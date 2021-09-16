package learn.barrel_of_books.controllers;

import learn.barrel_of_books.data.TransactionRepository;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
@AutoConfigureMockMvc
class TransactionControllerTest {
    @MockBean
    TransactionRepository repository;


}