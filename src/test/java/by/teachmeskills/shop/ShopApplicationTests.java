package by.teachmeskills.shop;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class ShopApplicationTests {
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    void contextLoads() {
    }

    @Test
    public void givenHomePageURI_whenMockMVC_thenReturnsHomePage() throws Exception {
        this.mockMvc.perform(get("/home")).andExpect(status().isOk());
    }

    @Test
    public void givenCategoryPageURIAndCategoryId_whenMockMVC_thenReturnsCategoryPage() throws Exception {
        this.mockMvc.perform(get("/category/1")).andExpectAll(
                status().isOk(),
                content().contentType("text/html;charset=UTF-8"));
    }

    @Test
    public void givenCategoryPageURIAndCategoryId_whenMockMVC_thenReturnsCsvWithCategoryProducts() throws Exception {
        this.mockMvc.perform(get("/product/csv/export/1")).andExpectAll(
                status().isOk(),
                content().contentType("text/csv;charset=UTF-8"));
    }

    @Test
    public void givenLoginPageURIAndNotExistedUser_whenMockMVC_thenPrintError() throws Exception {
        mockMvc.perform(post("/login?email=anton@gmail.com&password=A!1sssssssS"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    public void givenRegistrationPageURIAndNewUser_whenMockMVC_thenPrintHomePageOrReturnRegistrationPageWithErrorMessage() throws Exception {
        mockMvc.perform(post("/registration?" +
                        "name=Sergey" +
                        "&surname=alshuk" +
                        "&birthday=1994-02-01" +
                        "&email=sergey@gmail.com" +
                        "&password=sAA__!2bbbbbbbbbbbS"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"));
    }

    @Test
    public void givenLoginPageURIAndExistedUser_whenMockMVC_thenReturnHomePage() throws Exception {
        mockMvc.perform(post("/login?email=anton@gmail.com&password=$2a$10$3M31v/EvxImgOuhozSy6yeonBaqFBfWLzzeVITcEivdlt56rxlhtS"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"));
    }

    @Test
    public void givenLoginPageURIAndNotExistedUser_whenMockMVC_thenReturnErrorPage() throws Exception {
        mockMvc.perform(post("/login?email=yura@gmail.com&password=!!adadkiaaw"))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(content().contentType("text/html;charset=UTF-8"));
    }

    @Test
    public void givenSearchPageURIAndSearchParameter_whenMockMVC_thenTakeProductsContainSearchParameter() throws Exception {
        mockMvc.perform(post("/search?searchKey=муж"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"));
    }
}
