package org.example.eurovision_manager.model.repository;

import org.example.eurovision_manager.connection.ConnectionFactory;
import org.example.eurovision_manager.model.entity.Entry;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EntryRepository {

    public List<Entry> findAll() {
        List<Entry> entries = new ArrayList<>();
        String query = "SELECT * FROM entries ORDER BY id ASC";
        try (Connection connection = ConnectionFactory.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                entries.add(new Entry(rs.getInt("id"), rs.getString("country"), rs.getString("artist"), rs.getString("title"), rs.getInt("release_year")));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return entries;
    }

    public List<Entry> search(String keyword) {
        List<Entry> entries = new ArrayList<>();
        String query = "SELECT * FROM entries WHERE country ILIKE ? OR artist ILIKE ? OR title ILIKE ?";
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            String match = "%" + keyword + "%";
            ps.setString(1, match);
            ps.setString(2, match);
            ps.setString(3, match);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                entries.add(new Entry(rs.getInt("id"), rs.getString("country"), rs.getString("artist"), rs.getString("title"), rs.getInt("release_year")));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return entries;
    }

    public List<Entry> findAllSorted(String column, String direction) {
        List<Entry> entries = new ArrayList<>();
        String query = "SELECT * FROM entries ORDER BY " + column + " " + direction;
        try (Connection connection = ConnectionFactory.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                entries.add(new Entry(rs.getInt("id"), rs.getString("country"), rs.getString("artist"), rs.getString("title"), rs.getInt("release_year")));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return entries;
    }

    public void create(Entry entry) {
        String query = "INSERT INTO entries (country, artist, title, release_year) VALUES (?, ?, ?, ?)";
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, entry.getCountry());
            ps.setString(2, entry.getArtist());
            ps.setString(3, entry.getTitle());
            ps.setInt(4, entry.getReleaseYear());
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void update(Entry entry) {
        String query = "UPDATE entries SET country=?, artist=?, title=?, release_year=? WHERE id=?";
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, entry.getCountry());
            ps.setString(2, entry.getArtist());
            ps.setString(3, entry.getTitle());
            ps.setInt(4, entry.getReleaseYear());
            ps.setInt(5, entry.getId());
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void delete(int id) {
        String query = "DELETE FROM entries WHERE id = ?";
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public Entry findById(int id) {
        String query = "SELECT * FROM entries WHERE id = ?";
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Entry(rs.getInt("id"), rs.getString("country"), rs.getString("artist"), rs.getString("title"), rs.getInt("release_year"));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }
}