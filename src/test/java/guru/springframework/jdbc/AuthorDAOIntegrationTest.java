package guru.springframework.jdbc;

import guru.springframework.jdbc.dao.AuthorDAO;
import guru.springframework.jdbc.domain.Author;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Profile;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Profile("local")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AuthirDAOIntegrationTest {

    @Autowired
    AuthorDAO authorDAO;

    @Test
    void testGetAuthor() {
        Author author = authorDAO.getById(1L);
        assertThat(author).isNotNull();
    }
}
