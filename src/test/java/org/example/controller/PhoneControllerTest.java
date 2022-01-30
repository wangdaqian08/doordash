package org.example.controller;

import org.example.repository.PhoneRepository;
import org.example.service.DumpService;
import org.example.service.PhoneService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.example.controller.PhoneController.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebMvcTest(PhoneController.class)
public class PhoneControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PhoneService phoneService;

    @MockBean
    private DumpService dumpService;

    @MockBean
    private PhoneRepository phoneRepository;


    @Test
    public void testSavePhoneNumber() throws Exception {
        String data = "{\"raw_phone_numbers\": \"(Home) 415-415-4155 (Cell) 415-123-4567\"}";
        mockMvc.perform(post(PhoneController.DOORDASH_BASE_URL + SAVE_PHONE_NUMBERS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(data))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void testSavePhoneNumberInvalidInput() throws Exception {
        String data = "{\"test\": \"(Home) 415-415-4155 (Cell) 415-123-4567\"}";
        mockMvc.perform(post(PhoneController.DOORDASH_BASE_URL + SAVE_PHONE_NUMBERS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(data))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testSavePhoneNumberEmptyInput() throws Exception {
        String data = "{}";
        mockMvc.perform(post(PhoneController.DOORDASH_BASE_URL + SAVE_PHONE_NUMBERS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(data))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testCheckTask() throws Exception {
        String taskId = "taskId";
        mockMvc.perform(get(PhoneController.DOORDASH_BASE_URL + CHECK_TASK + "/" + taskId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void testCheckTaskInvalidParam() throws Exception {
        String taskId = "";
        mockMvc.perform(get(PhoneController.DOORDASH_BASE_URL + CHECK_TASK + "/" + taskId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testDumpData() throws Exception {
        mockMvc.perform(get(PhoneController.DOORDASH_BASE_URL + DUMP_DATA)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }

}
