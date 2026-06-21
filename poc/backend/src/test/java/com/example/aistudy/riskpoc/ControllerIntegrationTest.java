package com.example.aistudy.riskpoc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void customerListReturnsUnifiedResponseAndRequestId() throws Exception {
        mockMvc.perform(get("/api/customers")
                        .param("pageNo", "1")
                        .param("pageSize", "2")
                        .header("X-Request-Id", "REQ-TEST-CUSTOMER-LIST")
                        .header("X-Operator", "demo-query"))
                .andExpect(status().isOk())
                .andExpect(header().string("X-Request-Id", "REQ-TEST-CUSTOMER-LIST"))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.code", is("000000")))
                .andExpect(jsonPath("$.requestId", is("REQ-TEST-CUSTOMER-LIST")))
                .andExpect(jsonPath("$.data.records[0].customerNo").exists())
                .andExpect(jsonPath("$.data.pageNo", is(1)));
    }

    @Test
    void invalidRiskLevelReturnsContractErrorCode() throws Exception {
        mockMvc.perform(get("/api/customers")
                        .param("riskLevel", "UNKNOWN")
                        .header("X-Request-Id", "REQ-TEST-INVALID"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.code", is("400001")))
                .andExpect(jsonPath("$.requestId", is("REQ-TEST-INVALID")));
    }

    @Test
    void closedCustomerRecalculateReturnsConflict() throws Exception {
        mockMvc.perform(post("/api/customers/CUST100006/risk/recalculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}")
                        .header("X-Request-Id", "REQ-TEST-CLOSED")
                        .header("X-Operator", "demo-admin"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.code", is("409001")))
                .andExpect(jsonPath("$.message", is("关闭状态客户不允许触发风险重算")));
    }
}
