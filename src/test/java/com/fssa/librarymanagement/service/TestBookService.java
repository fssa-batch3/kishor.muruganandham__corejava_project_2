package com.fssa.librarymanagement.service;

import com.fssa.librarymanagement.exceptions.ServiceException;
import com.fssa.librarymanagement.model.Book;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TestBookService {

	private BookService bookService;
	private Book book;

	@BeforeEach
	public void setUp() {
		bookService = new BookService();
		book = new Book();
		book.setAuthor("Kishor");
		book.setGenre("Action");
		book.setDescription("Description");
		book.setTitle("Title");
		book.setLanguage("Eng");
		book.setPublisher("Publisher");
		book.setTotalCopies(10);
		book.setAvailableCopies(5);
		book.setLoanedCopies(2);
		book.setCoverImage("image");
	}

	@Test
	@Order(1)
	void testValidAddBook() {
		try {
			String result = bookService.addBook(book);
			assertEquals("Book added successfully", result);
		} catch (ServiceException e) {
			e.printStackTrace();
			fail("Should not throw ServiceException");
		}
	}

	@Test
	@Order(2)
	void testInvalidAddBook() {
		Book invalidBook = new Book();
		invalidBook.setTitle("");
		invalidBook.setTotalCopies(-1);
		invalidBook.setCoverImage("image");

		ServiceException result = assertThrows(ServiceException.class, () -> bookService.addBook(invalidBook));
		assertEquals("Failed to add book", result.getMessage());
	}

	@Test
	@Order(3)
	void testValidGetBookByName() {
		try {
			Book bookFromDB = bookService.getBookByName(book.getTitle());
			assertNotNull(bookFromDB, "Book should exist");
			assertEquals(book.getTitle(), bookFromDB.getTitle());
		} catch (ServiceException e) {
			e.printStackTrace();
			fail("Should not throw ServiceException");
		}
	}

	@Test
	@Order(4)
	void testInvalidGetBookByName() {
		ServiceException result = assertThrows(ServiceException.class, () -> bookService.getBookByName("No Title"));
		assertEquals("Failed to get book details", result.getMessage());
	}

	@Test
	@Order(5)
	void testListAllBooks() {
		try {
			List<Book> allBooks = bookService.listAllBooks();
			assertNotNull(allBooks, "List of books should not be null");
			for (Book b : allBooks) {
				System.out.println(b.getTitle());
			}
		} catch (ServiceException e) {
			e.printStackTrace();
			fail("Should not throw ServiceException");
		}
	}

	@Test
	@Order(6)
	void testValidUpdateBook() {
		try {
			Book existingBook = bookService.getBookByName(book.getTitle());
			assertNotNull(existingBook, "Book should exist");

			existingBook.setDescription("Updated description");
			Book updatedBook = bookService.updateBook(existingBook);

			assertNotNull(updatedBook, "Updated book should not be null");
			assertEquals(existingBook.getDescription(), updatedBook.getDescription(), "Description should be updated");
		} catch (ServiceException e) {
			e.printStackTrace();
			fail("Should not throw ServiceException");
		}
	}

	@Test
	@Order(7)
	void testInvalidUpdateBook() {
		Book book = new Book();
		book.setTitle("No Title");
		book.setDescription("Invalid description");

		ServiceException result = assertThrows(ServiceException.class, () -> bookService.updateBook(book));
		assertEquals("Failed to Update Book", result.getMessage());
	}

	@Test
	@Order(8)
	void testValidDeleteBook() {
		try {
			Object existingObject = bookService.getBookByName(book.getTitle());
			assertNotNull(existingObject, "Book should exist");

			boolean isDeleted = bookService.deleteBook(book.getTitle());
			assertTrue(isDeleted, "Book should be deleted successfully");
		} catch (ServiceException e) {
			e.printStackTrace();
			fail("Should not throw ServiceException");
		}
	}

	@Test
	@Order(9)
	void testInvalidDeleteBook() {
		ServiceException result = assertThrows(ServiceException.class, () -> bookService.deleteBook("No Title"));
		assertEquals("Failed to Delete Book", result.getMessage());
	}
}
