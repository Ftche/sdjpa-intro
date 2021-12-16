package guru.springframework.jdbc.dao;

import guru.springframework.jdbc.domain.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class AuthorDaoImpl implements AuthorDao {

        private final JdbcTemplate jdbcTemplate;

        public AuthorDaoImpl(final JdbcTemplate jdbcTemplate) {
                this.jdbcTemplate = jdbcTemplate;
        }

        @Override
        public Author getById(Long id) {
                String sql = "select author.id as id, first_name, last_name, book.id as book_id, book.isbn, book.publisher, book.title from author\n" +
                        "left outer join book on author.id = book.author_id where author.id = ?";
                return jdbcTemplate.query(sql, new AuthorExtractor(), id);
        }

        @Override
        public Author findAuthorByName(String firstName, String lastName) {
                return jdbcTemplate.queryForObject("SELECT * FROM author WHERE first_name = ? and last_name = ?",
                        getRowMapper(),
                        firstName, lastName);
        }

        @Override
        public void deleteAuthorById(Long id) {
                jdbcTemplate.update("DELETE FROM author WHERE id = ?", id);
        }

        @Override
        public Author updateAuthor(Author author) {
                jdbcTemplate.update("UPDATE author SET first_name = ? , last_name = ? WHERE id = ?",
                        author.getFirstName(), author.getLastName(),
                        author.getId());
                return this.getById(author.getId());
        }

        @Override
        public Author saveNewAuthor(Author author) {
                 jdbcTemplate.update("INSERT INTO author (first_name, last_name) VALUES (?,?)",
                        author.getLastName(),
                        author.getFirstName());

                 Long createdId = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);
                return this.getById(createdId);
        }

        private RowMapper<Author> getRowMapper(){
                return new AuthorMapper();
        }
}
