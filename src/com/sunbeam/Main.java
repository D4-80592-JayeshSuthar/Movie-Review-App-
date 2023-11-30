package com.sunbeam;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Main {

	public static Scanner sc;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		sc = new Scanner(System.in);
		clientOperations();
		sc.close();
	}

	public static void clientOperations() {
		int choice;
		User curUser = null;
		do {
			System.out.println("\n0. Exit\n1. Sign Up\n2. Sign In\nEnter choice: ");
			choice = sc.nextInt();
			switch (choice) {
			case 1:
				signUp();
				break;
			case 2:
				curUser = signIn();
				if (curUser != null) {
					userOperations(curUser);
				} else {
					System.out.println("Enter a valid user id and password !");
				}
				break;
			case 0:
				System.out.println("Exit done ! Bye !");
				break;
			default:
				break;

			}
		} while (choice != 0);
	}

	public static void signUp() {
		// TODO Auto-generated method stub
		System.out.print("First Name: ");
		String fname = sc.next();
		System.out.print("Last Name: ");
		String lname = sc.next();
		System.out.print("Email: ");
		String email = sc.next();
		System.out.print("Password: ");
		String passwd = sc.next();
		System.out.print("Mobile: ");
		String mobile = sc.next();
		System.out.print("Birth Date : ");
		String birth = sc.next();
		if (fname.isEmpty() || lname.isEmpty() || email.isEmpty() || passwd.isEmpty() || mobile.isEmpty()
				|| birth.isEmpty()) {
			System.out.println("All fields must be filled.");
			return;
		}
		User u = new User(0, fname, lname, email, passwd, mobile, birth);
		try (UserDao dao = new UserDao()) {
			int count = dao.save(u);
			System.out.println("User registered: " + count);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static User signIn() {
		// TODO Auto-generated method stub
		String email;
		String password;
		System.out.print("Enter email: ");
		email = sc.next();
		System.out.print("Enter password: ");
		password = sc.next();

		if (email.isEmpty() || password.isEmpty()) {
			System.out.println("All fields must be filled.");
			return null;
		}

		try (UserDao dao = new UserDao()) {
			User u = dao.findByEmail(email);
			if (u != null && password.equals(u.getPassword())) {
				return u;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static void editProfile(User curUser) {
		System.out.println("Enter password : ");
		String passwd = sc.next().trim();
		sc.nextLine();
		if (passwd.equals(curUser.getPassword())) {
			System.out.println("Enter new First Name : ");
			String newFName = sc.next().trim();
			sc.nextLine();
			System.out.println("Enter new Last Name : ");
			String newLName = sc.next().trim();
			sc.nextLine();
			System.out.println("Enter new email : ");
//			sc.nextLine(); 
			String newEmail = sc.next().trim();
//			sc.nextLine();

			if (newFName.isEmpty() || newLName.isEmpty() || newEmail.isEmpty()) {
				System.out.println("All fields must be filled.");
				return;
			}

			try (UserDao dao = new UserDao()) {
				int count = dao.update(curUser.getId(), newFName, newLName, newEmail);
				System.out.println("Profile edited ! rows affected :" + count);
				return;
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		} else {
			System.out.println("Enter valid current password ! ");
			return;
		}

	}

	private static User userOperations(User curUser) {
		// TODO Auto-generated method stub
		int choice;
		do {
			System.out.println("=====Menu=========");
			System.out.println("0. Sign Out\n" + "1. Edit Profile\n" + "2. Change Password\n" + "3.Write a review\n"
					+ "4. Edit Review\n" + "5. Display all movies\n" + "6. Display all reviews\n"
					+ "7. Display my reviews\n" + "8. Display reviews shared with me\n" + "9.Share a review\n"
					+ "10.Delete a review ");
			System.out.println("==================");
			System.out.println("Enter Choice : ");

			choice = sc.nextInt();
//	        sc.nextLine();
			switch (choice) {
			case 0:
				System.out.println("Signed Out !");
				break;
			case 1:// Edit Profile
				editProfile(curUser);
				break;
			case 2:// Change Password
				changeMyPassword(curUser);
				break;
			case 3:// Write a review
				addNewReview(curUser);
				break;
			case 4:// Edit Review
				editMyReview(curUser);
//				toggleQuote(curUser);
				break;
			case 5:// Display all movies
				displayAllMovies();
				break;
			case 6:// Display all reviews
				displayAllReviews();
				break;
			case 7:// Display my reviews
				displayMyReviews(curUser);
				break;
			case 8:// Display reviews shared with me
				displayShared(curUser);
				break;
			case 9:// Share a review
				shareReview(curUser);
				break;
			case 10:// .Delete a review
				try {
					deleteMyReview(curUser);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			default:
				System.out.println("Invalid Choice !");
				break;
			}

		} while (choice != 0);

		return null;
	}

	public static void displayMyReviews(User curUser) {
		// TODO Auto-generated method stub
		try (ReviewDao dao = new ReviewDao()) {
			List<Review> list = dao.findReviewsById(curUser.getId());
			System.out.println("My Reviews : ");
			list.forEach(System.out::println);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void displayShared(User curUser) {
		try (ReviewDao dao = new ReviewDao()) {
			List<Review> list = dao.findSharedReviews(curUser.getId());
			System.out.println("Shared Reviews : ");
			if (!list.isEmpty()) {
				for (Review review : list) {
					System.out.println(review);
				}
			} else {
				System.out.println("No shared reviews found.");
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public static void changeMyPassword(User curUser) {
//		sc.nextLine(); 
		System.out.print("Enter curr password : ");
		String passwd = sc.next().trim();
		if (passwd.isEmpty()) {
			System.out.println("Password cannot be empty ");
			return;
		}
		if (passwd.equals(curUser.getPassword())) {
			System.out.println("Enter new password : ");
			String newPasswd = sc.next().trim();
			if (newPasswd.isEmpty()) {
				System.out.println("Password cannot be empty ");
				return;
			}
			sc.nextLine();

			try (UserDao dao = new UserDao()) {
				curUser.setPassword(newPasswd);
				int count = dao.updatePassword(curUser.getEmail(), newPasswd);
				System.out.println("Password Updated Succesfully !  Rows Affected : " + count);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			return;
		} else {
			System.out.println("Enter valid current password ! ");
			return;
		}

	}

	public static void displayAllMovies() {
		// TODO Auto-generated method stub
		try (MovieDao dao = new MovieDao()) {
			List<Movies> list = dao.findAll();
			list.forEach(e -> System.out.println(e));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void displayAllUsers() {
		// TODO Auto-generated method stub
		try (UserDao dao = new UserDao()) {
			List<User> list = dao.findAll();
			list.forEach(e -> System.out.println(e));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void displayAllReviews() {
		// TODO Auto-generated method stub
		try (ReviewDao dao = new ReviewDao()) {
			List<Review> list = dao.findAll();
			list.forEach(e -> System.out.println(e));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String addNewReview(User curUser) {

		displayAllMovies();

		System.out.print("Enter your Movie Id : ");
		int id = sc.nextInt();

		if (!isValidMovieId(id)) {
			System.out.println("Invalid Movie ID ! ");
			return null;
		}
		if (!isValidReviewId(curUser.getId(), id)) {
			System.out.println("You have already given a review for this movie you can edit ! ");
			return null;
		}

		System.out.println("Enter rating : ");
		int rating = sc.nextInt();

		sc.nextLine();

		System.out.print("Enter review: ");
		String review = sc.nextLine();

		if (review.isEmpty()) {
			System.out.println("Review cannot be empty.");
			return null;
		}

		Timestamp timestamp = new Timestamp(new Date().getTime());

		Review r = new Review(0, id, review, rating, curUser.getId(), timestamp);

		try (ReviewDao dao = new ReviewDao()) {
			int count = dao.save(r);
			System.out.println("New review added: " + count);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return review;
	}

	private static boolean isValidMovieId(int movieId) {
		try (MovieDao dao = new MovieDao()) {
			List<Movies> movies = dao.findAll();
			for (Movies movie : movies) {
				if (movie.getId() == movieId) {
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	private static boolean isValidReviewId(int userId, int movieId) {
		try (ReviewDao dao = new ReviewDao()) {
			List<Review> reviews = dao.findAll();
			for (Review review : reviews) {
				if (review.getMovieId() == movieId && userId == review.getUserId()) {
					return false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	public static void shareReview(User curUser) {

		displayMyReviews(curUser);

		System.out.println("Enter review id to be shared : ");
		int rid = sc.nextInt();

		if (!isReviewBelongsToUser(rid, curUser.getId())) {
			System.out.println("You can only share your own review.");
			return;
		}

		Review curReview = getReviewById(rid);

		if (curReview == null) {
			System.out.println("Invalid review ID.");
			return;
		}

		System.out.println(curReview.toString());

		displayAllUsers();

		System.out.println("Enter the user ids to share the review with (enter 0 to end) :");

		List<Integer> sharedUserIds = new ArrayList<>();
		int userId;
		do {
			userId = sc.nextInt();
			if (userId != 0) {
				if (userId == curUser.getId()) {
					System.out.println("You cannot share your own review with yourself.");
				} else {
					sharedUserIds.add(userId);
				}
			}
		} while (userId != 0);

		try (ReviewDao dao = new ReviewDao()) {
			int count = dao.shareReview(curReview.getId(), sharedUserIds);
			System.out.println("Reviews shared with " + count + " users !");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void editMyReview(User curUser) {

		displayMyReviews(curUser);

		System.out.println("Enter id of review you want to edit : ");
		int rid = sc.nextInt();

		if (!isReviewBelongsToUser(rid, curUser.getId())) {
			System.out.println("You can only edit your own review.");
			return;
		}

		Review curReview = getReviewById(rid);

		if (curReview == null) {
			System.out.println("Invalid review ID.");
			return;
		}

		System.out.println(curReview.toString());

//		System.out.println(
//				"\nCurrent Review : " + "\n" + curReview.getReview() + "\n Rating : " + curReview.getRating() + "\n");

		System.out.print("Enter new rating : ");
		int rating = sc.nextInt();
//		if (rating is empty) {
//			System.out.println("rating cannot be empty.");
//			return null;
//		}

		System.out.print("Enter your new review : ");
		sc.nextLine();
		String reviewText = sc.nextLine().trim();
		if (reviewText.isEmpty()) {
			System.out.println("Review cannot be empty.");
			return;
		}

		Timestamp timestamp = new Timestamp(new Date().getTime());

		Review newReview = new Review(rid, curReview.getMovieId(), reviewText, rating, curUser.getId(), timestamp);

		try (ReviewDao dao = new ReviewDao()) {
			int count = dao.update(newReview);
			System.out.println("New review added: " + count);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static boolean isReviewBelongsToUser(int reviewId, int userId) {
		try (ReviewDao dao = new ReviewDao()) {
			Review review = dao.findReviewById(reviewId);
			return review != null && review.getUserId() == userId;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static Review getReviewById(int reviewId) {
		try (ReviewDao dao = new ReviewDao()) {
			return dao.findReviewById(reviewId);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void deleteMyReview(User curUser) throws Exception {
		System.out.println("Enter id of review you want to delete : ");
		int rid = sc.nextInt();

		if (!isReviewBelongsToUser(rid, curUser.getId())) {
			System.out.println("You can only delete your own reviews ! ");
			return;
		}

		try (ReviewDao dao = new ReviewDao()) {
			int count = dao.delete(rid);
			System.out.println("Review deleted :" + count);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
