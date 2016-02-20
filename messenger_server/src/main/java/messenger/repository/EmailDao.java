package messenger.repository;

import messenger.models.Email;
import messenger.models.Page;
import messenger.models.Status;
import messenger.repository.data.CRUDRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * Created by Narcis2007 on 20.12.2015.
 */

@Repository
public class EmailDao implements CRUDRepository<Email,Integer> {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Override
    public int count(String receiver) {
        List<Integer> count = jdbcTemplate.query(
                "SELECT COUNT(*) as count FROM mesaje where receiver= '" + receiver + "'",
                (rs, rowNum) -> {
                    return new Integer(rs.getInt("count"));
                }
        );
        return count.get(0);
    }


    public Collection<Email> findByUsername(String username)
    {
        return jdbcTemplate.query(
                "SELECT id, title, content, sender,receiver,status FROM mesaje where receiver= '"+username+"'",
                (rs, rowNum) -> {
                    Email email = new Email(rs.getString("title"), rs.getString("content"), rs.getString("sender"),rs.getString("receiver"));
                    email.setID(rs.getInt("id"));
                    email.setStatus(Status.valueOf(rs.getString("status")));
                    return email;
                }
        );
    }


    @Override
    public Email find(Integer id)
    {
        return null;
    }

    @Override
    public Collection<Email> getAll() {
        return jdbcTemplate.query(
                "SELECT id, title, content, sender,receiver,status FROM mesaje",
                (rs, rowNum) -> {
                    Email email = new Email(rs.getString("title"), rs.getString("content"), rs.getString("sender"),rs.getString("receiver"));
                    email.setID(rs.getInt("id"));
                    email.setStatus(Status.valueOf(rs.getString("status")));
                    return email;
                }
        );
    }

    @Override
    public void save(Email email) {
        jdbcTemplate.update("INSERT INTO mesaje (title,content,sender,receiver,status) VALUES (?,?,?,?,?)",email.getTitle(),email.getContent(),email.getSender(),email.getReceiver(),email.getStatus().toString());


    }

    @Override
    public boolean update(Email email) {
        return false;
    }

    @Override
    public boolean delete(Integer id) {
        return false;
    }

    public List<Email> getPage(String receiver,int pageNr,int pageSize) {
        return jdbcTemplate.query(
                "SELECT * FROM mesaje where receiver= '"+receiver+"' LIMIT "+pageNr*pageSize + ", "+pageSize,
                (rs, rowNum) -> {
                    Email email = new Email(rs.getString("title"), rs.getString("content"), rs.getString("sender"),rs.getString("receiver"));
                    email.setID(rs.getInt("id"));
                    email.setStatus(Status.valueOf(rs.getString("status")));
                    return email;
                }
        );
    }
}
