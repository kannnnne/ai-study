import { ElMessage } from 'element-plus';
import type { ApiErrorInfo, ApiResponse } from './types';

interface RequestOptions {
  method?: 'GET' | 'POST';
  params?: Record<string, unknown>;
  body?: unknown;
  operator?: 'demo-query' | 'demo-admin';
  notifyError?: boolean;
}

export class ApiError extends Error implements ApiErrorInfo {
  code: string;
  requestId: string;
  timestamp?: string;
  httpStatus?: number;

  constructor(info: ApiErrorInfo) {
    super(info.message);
    this.name = 'ApiError';
    this.code = info.code;
    this.requestId = info.requestId;
    this.timestamp = info.timestamp;
    this.httpStatus = info.httpStatus;
  }
}

const apiBase = import.meta.env.VITE_API_BASE ?? '';

function buildRequestId() {
  const suffix = Math.random().toString(16).slice(2, 8).toUpperCase();
  return `REQ-FE-${Date.now()}-${suffix}`;
}

function toQuery(params?: Record<string, unknown>) {
  if (!params) return '';
  const search = new URLSearchParams();
  Object.entries(params).forEach(([key, value]) => {
    if (value === undefined || value === null || value === '') return;
    search.append(key, String(value));
  });
  const query = search.toString();
  return query ? `?${query}` : '';
}

export async function request<T>(path: string, options: RequestOptions = {}): Promise<ApiResponse<T>> {
  const requestId = buildRequestId();
  const method = options.method ?? 'GET';
  const response = await fetch(`${apiBase}${path}${toQuery(options.params)}`, {
    method,
    headers: {
      'Content-Type': 'application/json',
      'X-Request-Id': requestId,
      'X-Operator': options.operator ?? 'demo-query'
    },
    body: method === 'POST' ? JSON.stringify(options.body ?? {}) : undefined
  });

  const payload = (await response.json()) as ApiResponse<T | null>;
  const responseRequestId = response.headers.get('X-Request-Id') ?? payload.requestId ?? requestId;

  if (!response.ok || !payload.success) {
    const error = new ApiError({
      code: payload.code ?? '500000',
      message: payload.message ?? '系统异常',
      requestId: responseRequestId,
      timestamp: payload.timestamp,
      httpStatus: response.status
    });
    if (options.notifyError !== false) {
      ElMessage.error(`${error.message}（${error.code} / ${error.requestId}）`);
    }
    throw error;
  }

  return {
    ...payload,
    requestId: responseRequestId,
    data: payload.data as T
  };
}

export function toApiError(error: unknown): ApiErrorInfo {
  if (error instanceof ApiError) {
    return {
      code: error.code,
      message: error.message,
      requestId: error.requestId,
      timestamp: error.timestamp,
      httpStatus: error.httpStatus
    };
  }
  return {
    code: '500000',
    message: error instanceof Error ? error.message : '系统异常',
    requestId: 'REQ-FE-UNKNOWN'
  };
}
