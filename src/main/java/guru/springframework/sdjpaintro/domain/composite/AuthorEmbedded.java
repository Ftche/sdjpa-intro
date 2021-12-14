package guru.springframework.sdjpaintro.domain.composite;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "author_composite")
public class AuthorEmbedded {

    @EmbeddedId
    private NameId nameId;

    private String country;

    public AuthorEmbedded() {
    }

    public AuthorEmbedded(final NameId nameId) {
        this.nameId = nameId;
    }

    public NameId getNameId() {
        return this.nameId;
    }

    public void setNameId(final NameId nameId) {
        this.nameId = nameId;
    }

    public String getCountry() {
        return this.country;
    }

    public void setCountry(final String country) {
        this.country = country;
    }
}
