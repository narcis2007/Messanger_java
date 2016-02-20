package messenger.repository;

import messenger.models.User;
import messenger.repository.data.CRUDRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by Narcis2007 on 16.12.2015.
 */
@Repository
public class UserDao implements CRUDRepository<User,Integer> {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private static final Logger log = Logger.getLogger( UserDao.class.getName() );

    @Override
    public int count(String receiver) {
       // return jdbcTemplate.query("SELECT count(*) as count FROM utilizatori", (rs, rowNum) -> new User(rs.getString("username"), rs.getString("password"));
        return jdbcTemplate.query(
                "SELECT id, username, password FROM utilizatori",
                (rs, rowNum) -> new User(rs.getString("username"), rs.getString("password"))
        ).size();//nu e bun schimb mai incolo
    }

    @Override
    public User find(Integer integer) {
        return null;
    }

    public User findByUsername(String username) {
        List<User> found = jdbcTemplate.query(
                "SELECT id, username, password FROM utilizatori where username= '" + username + "'",
                (rs, rowNum) -> new User(rs.getString("username"), rs.getString("password"))
        );
//        log.info(found.get(0).getUsername()+found.get(0).getPassword());
        if(found.size()>0)
            return found.get(0);
        return null;
    }



    @Override
    public Collection<User> getAll() {


        return jdbcTemplate.query(
                "SELECT id, username FROM utilizatori",
                (rs, rowNum) -> new User(rs.getString("username"))
        );
    }

    @Override
    public void save(User user) {
        jdbcTemplate.execute(
                "insert into utilizatori (username,password) values( '".concat(user.getUsername()).concat("','").concat(user.getPassword()).concat("')"));
    }

    @Override
    public boolean update(User user) {
        return false;
    }

    @Override
    public boolean delete(Integer integer) {
        return false;
    }
}
