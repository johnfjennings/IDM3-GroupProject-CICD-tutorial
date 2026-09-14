package ie.tus.gallery.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void loginPageIsPublicAndShowsHeading() throws Exception {
        // Week 1 tutorial PR #2: when you change the login heading,
        // this assertion fails — read the CI log, then update it to match.
        mockMvc.perform(get("/login"))
            .andExpect(status().isOk())
            .andExpect(content().string(org.hamcrest.Matchers.containsString(
                "<h1>Sign in to TUS Gallery</h1>")));
    }

    @Test
    void validLoginRedirectsToDashboard() throws Exception {
        mockMvc.perform(SecurityMockMvcRequestBuilders.formLogin()
                .user("student1").password("Password123!"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/dashboard"));
    }

    @Test
    void invalidLoginReturnsToLoginWithError() throws Exception {
        mockMvc.perform(SecurityMockMvcRequestBuilders.formLogin()
                .user("student1").password("wrong"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/login?error"));
    }

    @Test
    void dashboardRequiresAuthentication() throws Exception {
        mockMvc.perform(get("/dashboard"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrlPattern("**/login"));
    }
}
