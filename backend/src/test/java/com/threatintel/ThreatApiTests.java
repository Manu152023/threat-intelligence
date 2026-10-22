package com.threatintel;

import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class ThreatApiTests {

    @Autowired
    private MockMvc mvc;

    private static final String VALID = """
            {"title":"Test threat","description":"Description","severity":"HIGH",
             "category":"MALWARE","status":"ACTIVE","source":"CERT-EU"}
            """;

    @Test
    void createThreatReturns201() throws Exception {
        mvc.perform(post("/api/threats").contentType(MediaType.APPLICATION_JSON).content(VALID))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.severity").value("HIGH"));
    }

    @Test
    void missingTitleReturns400() throws Exception {
        String body = """
                {"title":"","description":"d","severity":"LOW","category":"OTHER","status":"ACTIVE"}
                """;
        mvc.perform(post("/api/threats").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.title").exists());
    }

    @Test
    void filterBySeverityOnlyReturnsMatches() throws Exception {
        mvc.perform(get("/api/threats").param("severity", "CRITICAL"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].severity", everyItem(is("CRITICAL"))));
    }

    @Test
    void unknownIdReturns404() throws Exception {
        mvc.perform(get("/api/threats/999999")).andExpect(status().isNotFound());
    }

    @Test
    void statsEndpointReturnsTotals() throws Exception {
        mvc.perform(get("/api/threats/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").exists())
                .andExpect(jsonPath("$.bySeverity.CRITICAL").exists());
    }
}
