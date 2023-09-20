/**
 * 
 */
package com.fssa.librarymanagement.dao;

import static com.fssa.librarymanagement.utils.ResultSetUtils.buildCommentFromResultSet;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.fssa.librarymanagement.exceptions.DAOException;
import com.fssa.librarymanagement.exceptions.DatabaseConnectionException;
import com.fssa.librarymanagement.model.Comment;
import com.fssa.librarymanagement.utils.ConnectionUtil;

/**
 * 
 */
public class CommentDAO {

	private static final String INSERT_COMMENT_QUERY = "INSERT INTO comments (user_id, book_id, description, created_at) "
			+ "VALUES (?, ?, ?, ?)";

	private static final String UPDATE_COMMENT_QUERY = "UPDATE comments SET description = ?, edited_at = ?, is_edited = TRUE WHERE comment_id = ?";

	private static final String DELETE_COMMENT_QUERY = "UPDATE comments SET is_active = false WHERE comment_id = ?";

	private static final String BASE_COMMENT_QUERY = "SELECT c.comment_id, c.description, c.created_at, c.edited_at, c.is_active, c.is_edited, "
			+ "u.user_id AS user_user_id, u.user_name, u.email_id, u.profile_image, "
			+ "b.book_id AS book_book_id, b.title, b.cover_image " + "FROM comments c "
			+ "JOIN users u ON c.user_id = u.user_id " + "JOIN books b ON c.book_id = b.book_id ";

	private static final String LIST_COMMENTS_BY_BOOK_QUERY = BASE_COMMENT_QUERY
			+ "WHERE c.book_id = ? AND c.is_active = true";

	private static final String LIST_ALL_COMMENTS_QUERY = BASE_COMMENT_QUERY + "WHERE c.is_active = true";

	public boolean createComment(Comment comment) throws DAOException {

		try (Connection connection = ConnectionUtil.getConnection();
				PreparedStatement pst = connection.prepareStatement(INSERT_COMMENT_QUERY)) {

			pst.setInt(1, comment.getUser().getUserId());
			pst.setInt(2, comment.getBook().getBookId());
			pst.setString(3, comment.getDescription());
			pst.setDate(4, Date.valueOf(comment.getCreatedAt()));

			int rowsAffected = pst.executeUpdate();
			return rowsAffected > 0;

		} catch (SQLException | DatabaseConnectionException e) {
			throw new DAOException(e);
		}
	}

	public boolean updateComment(Comment comment) throws DAOException {
		try (Connection connection = ConnectionUtil.getConnection();
				PreparedStatement pst = connection.prepareStatement(UPDATE_COMMENT_QUERY)) {

			pst.setString(1, comment.getDescription());
			pst.setDate(2, Date.valueOf(comment.getEditedAt()));
			pst.setBoolean(3, comment.isEdited());
			pst.setInt(4, comment.getCommentId());

			int rowsAffected = pst.executeUpdate();
			return rowsAffected > 0;

		} catch (SQLException | DatabaseConnectionException e) {
			throw new DAOException(e);
		}
	}

	public boolean deleteComment(int commentId) throws DAOException {
		try (Connection connection = ConnectionUtil.getConnection();
				PreparedStatement pst = connection.prepareStatement(DELETE_COMMENT_QUERY)) {

			pst.setInt(1, commentId);

			int rowsAffected = pst.executeUpdate();
			return rowsAffected > 0;

		} catch (SQLException | DatabaseConnectionException e) {
			throw new DAOException(e);
		}
	}

	public List<Comment> listCommentByBook(int bookId) throws DAOException {
		List<Comment> comments = new ArrayList<>();
		try (Connection connection = ConnectionUtil.getConnection();
				PreparedStatement pst = connection.prepareStatement(LIST_COMMENTS_BY_BOOK_QUERY)) {

			pst.setInt(1, bookId);

			try (ResultSet rs = pst.executeQuery()) {
				while (rs.next()) {
					Comment comment = buildCommentFromResultSet(rs);
					comments.add(comment);
				}
			}

		} catch (SQLException | DatabaseConnectionException e) {
			throw new DAOException(e);
		}
		return comments;
	}

	public List<Comment> listAllComments() throws DAOException {
		List<Comment> comments = new ArrayList<>();
		try (Connection connection = ConnectionUtil.getConnection();
				PreparedStatement pst = connection.prepareStatement(LIST_ALL_COMMENTS_QUERY)) {

			try (ResultSet rs = pst.executeQuery()) {
				while (rs.next()) {
					Comment comment = buildCommentFromResultSet(rs);
					comments.add(comment);
				}
			}

		} catch (SQLException | DatabaseConnectionException e) {
			throw new DAOException(e);
		}
		return comments;
	}

}