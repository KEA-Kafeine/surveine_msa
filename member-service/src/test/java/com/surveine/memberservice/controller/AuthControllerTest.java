package com.surveine.memberservice.controller;

import com.surveine.memberservice.domain.Member;
import com.surveine.memberservice.dto.MemberSignupReqDTO;
import com.surveine.memberservice.enums.GenderType;
import com.surveine.memberservice.security.AuthenticationFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.surveine.memberservice.config.Result;
import com.surveine.memberservice.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = true)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(new AuthController(authService)).build();
    }

//    public Member createMember(){
//        MemberSignupReqDTO reqDTO = MemberSignupReqDTO.builder()
//                .email("test@ex
//    }

    @Test
    public void testSignup() throws Exception {
        // Given
        MemberSignupReqDTO reqDTO = MemberSignupReqDTO.builder()
                .email("test@example.com")
                .name("John Doe")
                .password(passwordEncoder.encode("password123"))
                .birthday("2000-04-15")
                .gender(GenderType.MAN)
                .build();

        Result expectedResult = Result.builder()
                .isSuccess(true)
                .message("회원가입 요청 성공")
                .build();

        // When
        doNothing().when(authService).createMember(reqDTO);

        // Then
        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(reqDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.isSuccess").value(expectedResult.getIsSuccess()));
        verify(authService, times(1)).createMember(reqDTO);
        verifyNoMoreInteractions(authService);
    }

    @Test
    public void testSignupFailure() throws Exception {
        // Given
        MemberSignupReqDTO reqDTO = MemberSignupReqDTO.builder()
                .email("test@example.com")
                .name("John Doe")
                .password("password123")
                .birthday("2000-04-15")
                .gender(GenderType.MAN)
                .build();

        // When
        doThrow(new RuntimeException("이미 가입되어 있는 유저입니다")).when(authService).createMember(reqDTO);

        // Then
        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(reqDTO)))
                .andExpect(status().isBadRequest());

        verify(authService, times(1)).createMember(reqDTO);
        verifyNoMoreInteractions(authService);
    }

    private String asJsonString(Object obj) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
