package com.ticketpriorization;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.test.web.servlet.MvcResult;
import com.jayway.jsonpath.JsonPath;

@SpringBootTest
@AutoConfigureMockMvc

public class TicketsControllerTest {
    @Autowired
    private MockMvc mockmvc;

    @Test
    @DisplayName("Creating a ticket with valid fields should return status 201")
    void validTicketCreation() throws Exception{
        mockmvc.perform(post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                {
                    "title": "Profile page loading slowly",
                    "description": "The profile page takes several seconds to load.",
                    "severity": "SEV3"
                }
                """))
                .andExpect(status().isCreated());

    }

    @Test
    @DisplayName("Creating a ticket with a blank value should return status 400")
    void createBlankTicket() throws Exception {
        mockmvc.perform(post("/api/tickets")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                {
                    "title": "",
                    "description": "The profile page takes several seconds to load.",
                    "severity": "SEV3"
                }
                """))
                .andExpect(status().isBadRequest());

        mockmvc.perform(post("/api/tickets")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                {
                    "title": "Profile page loading slowly",
                    "description": "",
                    "severity": "SEV3"
                }
                """))
                .andExpect(status().isBadRequest());

        mockmvc.perform(post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                {
                    "title": "Profile page loading slowly",
                    "description": "The profile page takes several seconds to load.",
                    "severity": null
                }
                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Updating an existing ticket with blank/null should return status 400")
    void updateTicketBlankNull() throws Exception{
//        Create valid ticket to update
        mockmvc.perform(post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                {
                    "title": "Profile page loading slowly",
                    "description": "The profile page takes several seconds to load.",
                    "severity": "SEV3"
                }
                """))
                .andExpect(status().isCreated());

        mockmvc.perform(patch("/api/tickets/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                {
                    "title": "",
                    "description": "The profile page takes several seconds to load.",
                    "severity": "SEV3"
                }
                """))
                .andExpect(status().isBadRequest());

        mockmvc.perform(patch("/api/tickets/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                {
                    "title": "Profile page loading slowly",
                    "description": "",
                    "severity": "SEV3"
                }
                """))
                .andExpect(status().isBadRequest());

        mockmvc.perform(patch("/api/tickets/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                {
                    "title": "Profile page loading slowly",
                    "description": "The profile page takes several seconds to load.",
                    "severity": null
                }
                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Updating non existent ticket with valid fields should return status 404")
    void updateNonExistentTicket() throws Exception{
        mockmvc.perform(patch("/api/tickets/9999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                {
                    "title": "Profile page loading slowly",
                    "description": "The profile page takes several seconds to load.",
                    "severity": "SEV3"
                }
                """))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Updating an existing ticket with valid fields should return status 200")
    void updateTicketValid() throws Exception {
//      Create valid ticket to update
        MvcResult createResult = mockmvc.perform(post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                {
                    "title": "Profile page loading slowly",
                    "description": "The profile page takes several seconds to load.",
                    "severity": "SEV3"
                }
                """))
                .andExpect(status().isCreated()).andReturn();

        String ticketId = JsonPath.read(createResult.getResponse().getContentAsString(), "$.id");

        mockmvc.perform(patch("/api/tickets/" + ticketId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "title": "Profile page loading slowly!",
                                    "description": "The profile page takes several seconds to load!",
                                    "severity": "SEV1"
                                }
                                """))
                .andExpect(status().isOk());
    }
}
