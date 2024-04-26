package com.example.demo.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class SqlDatabase implements Database{

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public int saveUser(User user) {

        return jdbcTemplate.update(
                "insert into users (id, name, email) values(?,?,?)",
                user.getId(), user.getName(), user.getEmail());

    }

    @Override
    public int saveImage(Image image) {

        return jdbcTemplate.update(
                "insert into Image (id, image) values(?,?)",
                image.getId(), image.getImage());

    }

    @Override
    public User getUser(String Id) {
        String sql = "SELECT * FROM User WHERE id = ?";
        User target = jdbcTemplate.queryForObject(sql, (rs, rowNum) ->
                new User(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("email")
                ), new Object[] { Id });
        return target;
    }

    @Override
    public Image getImage(String userId) {
        String sql = "SELECT * FROM Image WHERE id = ?";
        Image target = jdbcTemplate.queryForObject(sql, (rs, rowNum) ->
                new Image(
                        rs.getString("id"),
                        rs.getBytes("image")
                ),new Object[]{userId});
        return target;

    }
}