package GUI;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.Collections;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

/**
 * The class implemented here deals with the population of the queue with the
 * random number of reservations (0-4 students). Each reservation has details
 * like student email id and optional questions but half the reservations has
 * questions. Each reservation should have a reservation time that is chosen
 * randomly from the current time, 5 minutes before the current time, or 11
 * minutes before the current time. After the first student is marked present
 * that particular reservation is removed from the queue. If the student is
 * marked absent then that student is either banned or moved to the end of the
 * queue. After every action the current state of the queue is also displayed.
 */
public class SEC_Part3 extends JPanel {

	private JFrame frame;
	static JButton b, b1, b2;

	JTextField textArea = new JTextField();

	/**
	 * Holds the current status of the reservation queue at any given time.
	 */
	Queue<Reservation> globalReservationQueue;

	/*
	 * Control of the Application starts
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new SEC_Part3();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/*
	 * The constructor invokes the initialize method which is responsible for the
	 * entire work flow of the reservation process
	 */
	public SEC_Part3() {
		initialize();
	}

	/**
	 * This method generates at most 4 random number of students with their
	 * reservation details and displays the required information of the students on
	 * the frame.
	 */
	private void initialize() {
		/* Get Random Number of Reservations */
		Random random = new Random();
		int numberOfReservations = random.nextInt(5);

		/* Populate the Reservation Queue */
		Queue<Reservation> reservationQueue = getReservationQueue(numberOfReservations);
		updateReservationQueue(reservationQueue);

		frame = new JFrame("Mark Attendance");
		ButtonGroup G1;
		G1 = new ButtonGroup();

		/* Remove the first reservation from the queue */
		Queue<Reservation> reservations = getGlobalreservations();
		Reservation reservation = reservations.peek();
		/*
		 * if the reservation queue is empty , show a message else a new frame is
		 * generated
		 */
		if (reservation == null) {
			JOptionPane.showMessageDialog(null, "There were no appointments on the reservation Queue !");
			frame.setVisible(false);
		} else {
			frame.setVisible(false);
			frame.getContentPane().setLayout(null);

			displayReservationDetails(frame, reservation);

			/* Radio buttons to mark student present or not present */
			JRadioButton rdbtnPresent = new JRadioButton("Present");
			rdbtnPresent.setBounds(69, 240, 155, 29);
			frame.getContentPane().add(rdbtnPresent);
			JRadioButton rdbtnNotPresent = new JRadioButton("Not Present");
			rdbtnNotPresent.setBounds(69, 280, 155, 29);
			frame.getContentPane().add(rdbtnNotPresent);
			/* Radio button group */
			G1.add(rdbtnPresent);
			G1.add(rdbtnNotPresent);

			/* Submit Attendance Button to report whether the student is present or not */
			JButton btnNewButton = new JButton("Submit Attendance");
			btnNewButton.setEnabled(false);
			/* Submit button enabled only if any radio button is selected */
			rdbtnPresent.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					btnNewButton.setEnabled(true);
				}
			});
			/* Submit button enabled only if any radio button is selected */
			rdbtnNotPresent.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					btnNewButton.setEnabled(true);
				}
			});
			/*
			 * method to determine the functionality of the student when a radio button is
			 * selected - present or not present
			 */
			btnNewButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					boolean present = rdbtnPresent.isSelected();
					boolean notPresent = rdbtnNotPresent.isSelected();
					if (!present && !notPresent)
						JOptionPane.showMessageDialog(null, "Please Mark Attendande First !");
					else {
						if (notPresent) {
							/* Get the reservation time and the current time */
							Calendar reservationTime = reservation.getReservationTime();
							Calendar currentTime = Calendar.getInstance();

							/* Compute the time difference */
							int timeDifference = getTimeDifferenceInMinutes(currentTime, reservationTime);

							/*
							 * If the student is 10 minutes or more late Set that student's record with a
							 * ban date equal to the current date Remove the reservation from the
							 * reservation queue
							 */
							if (timeDifference >= 10)
								banStudent(reservations);

							/*
							 * If the student is under 10 minutes late Re-add the reservation to the end of
							 * queue
							 */
							else if (timeDifference < 10)
								addReservationToEndOfQueue(reservations);
						} else
							/*
							 * If the student is present, remove that reservation from the reservation queue
							 */
							markStudentPresent(reservations);

						/* Update the current status of the global reservation queue */
						updateReservationQueue(reservations);
						frame.setVisible(false);

						/* A new frame to display the current status of the reservation queue */
						getCurrentQueueStatusFrame();
					}
				}
			});
			btnNewButton.setBounds(224, 320, 180, 29);
			frame.getContentPane().add(btnNewButton);
		}
	}

	/**
	 * This method shows the status of the current reservation queue initially as
	 * well as after every action of the status update
	 */
	public void getCurrentQueueStatusFrame() {
		JFrame f = new JFrame("Reservation Queue");
		f.setBounds(100, 100, 1000, 300);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.getContentPane().setLayout(null);

		/* Label for the Initial Reservation Queue display */
		JLabel lblNewLabel_1 = new JLabel("Current Reservation Queue");
		lblNewLabel_1.setBounds(20, 90, 220, 20);
		f.getContentPane().add(lblNewLabel_1);

		/* Label for the Initial Reservation Queue display */
		JLabel lblNewLabel_2 = new JLabel("Displayng Email Ids of the Students in the Reservation Queue");
		lblNewLabel_2.setBounds(240, 45, 500, 20);
		f.getContentPane().add(lblNewLabel_2);

		/* Populate the text area to display the current status of the queue */
		displayCurrentStatusOfQueue(f);
		f.setVisible(true);
	}

	/**
	 * This method is used to remove the student from the reservation queue once
	 * that student is marked with present status
	 * 
	 * @param reservations
	 */
	protected void markStudentPresent(Queue<Reservation> reservations) {
		reservations.poll();
	}

	/**
	 * This method displays the students details like email id, reseravation time as
	 * well as the reservation details for the TA once he is in.
	 * 
	 * @param f
	 * @param reservation
	 */
	public void displayReservationDetails(JFrame f, Reservation reservation) {
		/**
		 * Display Reservation Details
		 */
		JLabel lblReservationDetails = new JLabel();
		lblReservationDetails.setBounds(180, 23, 237, 20);
		lblReservationDetails.setText("RESERVATION DETAILS");
		f.getContentPane().add(lblReservationDetails);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(600, 400);
		f.setVisible(true);

		/**
		 * Display Student Email ID on the frame
		 */
		JLabel lblEmail = new JLabel();
		lblEmail.setBounds(69, 53, 237, 20);
		lblEmail.setText("STUDENT EMAIL ID : ");
		f.getContentPane().add(lblEmail);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(600, 400);
		f.setVisible(true);

		/**
		 * Shows the email id of the student on the frame
		 */
		JLabel lblNewLabel = new JLabel();
		lblNewLabel.setBounds(230, 53, 237, 20);
		lblNewLabel.setText(reservation.getStudent().getEmailId());
		f.getContentPane().add(lblNewLabel);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(600, 400);
		f.setVisible(true);

		/**
		 * Display Reservation Time Details on the frame
		 */
		JLabel lblReservationTime = new JLabel();
		lblReservationTime.setBounds(69, 73, 237, 20);
		lblReservationTime.setText("RESERVATION TIME : ");
		f.getContentPane().add(lblReservationTime);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(600, 400);
		f.setVisible(true);

		/**
		 * Displays the reservation time and details of the student on the frame
		 */
		JLabel lblReservationDateTime = new JLabel();
		lblReservationDateTime.setBounds(230, 73, 237, 20);
		lblReservationDateTime.setText(reservation.getReservationTime().getTime().toString());
		f.getContentPane().add(lblReservationDateTime);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(600, 400);
		f.setVisible(true);

		/**
		 * Displaying Questions for Student on the frame
		 */
		JLabel lblQuestions = new JLabel();
		lblQuestions.setBounds(69, 100, 237, 20);
		lblQuestions.setText("QUESTIONS : ");
		f.getContentPane().add(lblQuestions);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(600, 400);
		f.setVisible(true);

		JTextField textArea1 = new JTextField();
		textArea1.setBounds(69, 120, 452, 50);
		textArea1.setEditable(false);
		/**
		 * to display the question to be asked by the student
		 */
		String questions = reservation.getQuestions();
		if (questions == null)
			textArea1.setText("The Student has No Questions");
		else
			textArea1.setText(questions);
		f.getContentPane().add(textArea1);

		/**
		 * Displaying Radio Buttons for user to mark attendance of the student
		 */
		JLabel lblMarkAttendance = new JLabel();
		lblMarkAttendance.setBounds(69, 220, 237, 20);
		lblMarkAttendance.setText("MARK ATTENDANCE");
		f.getContentPane().add(lblMarkAttendance);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(600, 400);
		f.setVisible(true);
	}

	/**
	 * This method gets the time difference in the minutes to compare the time
	 * difference for the later processing based on the status
	 * 
	 * @param currentTime
	 * @param reservationTime
	 * @return
	 */
	public int getTimeDifferenceInMinutes(Calendar currentTime, Calendar reservationTime) {
		return Math.abs(currentTime.get(Calendar.MINUTE) - reservationTime.get(Calendar.MINUTE));
	}

	/**
	 * Sets the banned date to the student object
	 * 
	 * @param reservations
	 */
	public void banStudent(Queue<Reservation> reservations) {
		Student studentToBeBanned = reservations.poll().getStudent();
		studentToBeBanned.setBanDate(Calendar.getInstance());
	}

	/**
	 * This method adds the reservation details to the end of the queue
	 * 
	 * @param reservations with the reservation details of the student
	 */
	public void addReservationToEndOfQueue(Queue<Reservation> reservations) {
		Reservation reservationToBeAddedAtEnd = reservations.poll();
		reservations.add(reservationToBeAddedAtEnd);
	}

	/**
	 * This method gets the reservation queue details
	 * 
	 * @param numberOfReservations
	 * @return
	 */
	public Queue<Reservation> getReservationQueue(int numberOfReservations) {
		// Instantiating the reservation queue
		Queue<Reservation> reservations = new LinkedList<Reservation>();

		/*
		 * List of three reservation times : current time 5 minutes before current time;
		 * and 11 minutes before current time
		 */
		List<Calendar> listOfTimes = new ArrayList<Calendar>();
		listOfTimes.add(Calendar.getInstance());

		Calendar reservationTimeFiveMins = Calendar.getInstance();
		reservationTimeFiveMins.add(Calendar.MINUTE, -5);
		listOfTimes.add(reservationTimeFiveMins);

		Calendar reservationTimeElevenMins = Calendar.getInstance();
		reservationTimeElevenMins.add(Calendar.MINUTE, -11);
		listOfTimes.add(reservationTimeElevenMins);

		// Populating the reservation queue with random number of reservations
		ArrayList<Integer> numbers = new ArrayList<Integer>();
		for (int i = 1; i <= numberOfReservations; i++) {
			numbers.add(i);
		}
		Collections.shuffle(numbers);

		for (int i = 0; i < numberOfReservations; i++) {

			// Creating a reservation object
			int number = numbers.get(i);
			String emailId = "student_" + number + "@buffalo.edu";
			Student student = new Student(emailId);

			// Randomly selecting one of the three possible reservation times
			Random random = new Random();
			int randomIndex = random.nextInt(listOfTimes.size());
			Reservation reservation = new Reservation(student, listOfTimes.get(randomIndex));

			// Populating alternate reservations with questions
			if ((number % 2) != 0) {
				reservation.setQuestions("Questions for the Student : \n Q.1. First Question \n Q.2. SecondQuestion");
			}
			reservations.add(reservation);
		}
		return reservations;
	}

	/**
	 * This method displays the current state of the queue with the reservations
	 * present in the queue.
	 * 
	 * @param frame
	 */
	public void displayCurrentStatusOfQueue(JFrame frame) {
		textArea.setEditable(false);
		StringBuilder sb = new StringBuilder();
		Queue<Reservation> reservationQueueCopy = getReservationQueueCopy(getGlobalreservations());
		/* if the reservation queue is empty or otherwise */
		if (reservationQueueCopy.isEmpty())
			sb.append("There were no appointments on the reservation Queue !");
		while (!reservationQueueCopy.isEmpty()) {
			sb.append(reservationQueueCopy.poll().getStudent().getEmailId() + ",  ");
		}
		textArea.setText(String.valueOf(sb));
		textArea.setBounds(230, 90, 700, 29);
		frame.getContentPane().add(textArea);
	}

	/**
	 * This method returns the reservation queue details at any point on the queue.
	 * 
	 * @return global reservation queue
	 */
	public Queue<Reservation> getGlobalreservations() {
		return globalReservationQueue;
	}

	/**
	 * This method updates the reservation queue after the change of the status by
	 * the TA - present or not present
	 * 
	 * @param reservationQueue
	 */
	public void updateReservationQueue(Queue<Reservation> reservationQueue) {
		globalReservationQueue = getReservationQueueCopy(reservationQueue);
	}

	/**
	 * This method fetches the reservation queue which contains the details of the
	 * reservation of the student
	 * 
	 * @param reservationQueue
	 * @return
	 */
	public Queue<Reservation> getReservationQueueCopy(Queue<Reservation> reservationQueue) {
		return new LinkedList<>(reservationQueue);
	}

}

/**
 * The Reservation class which contains the details of the reservation along
 * with the student details kept in the student object
 */
class Reservation {
	/**
	 * A student object to keep the track of the student details
	 */
	private Student student;

	/**
	 * A String to keep the question of the student
	 */
	private String questions;

	/**
	 * Data and Time of Calendar type to keep the reservation time
	 */
	private Calendar reservationTime;

	/**
	 * Constructor of the Reservation class with Student and Reservation Time
	 * 
	 * @param student
	 * @param reservationTime
	 */
	public Reservation(Student student, Calendar reservationTime) {
		this.student = student;
		this.reservationTime = reservationTime;
	}

	/**
	 * This method gets the student object which contains the details of the student
	 * like email id, reservation date.
	 * 
	 * @return student object which has the details of the student
	 */
	public Student getStudent() {
		return this.student;
	}

	/**
	 * This method returns the questions of the students to be asked (optional)
	 * 
	 * @return questions of the student
	 */
	public String getQuestions() {
		return this.questions;
	}

	/**
	 * This method sets the reservation time of the student
	 * 
	 * @return reservation time of the student
	 */
	public Calendar getReservationTime() {
		return this.reservationTime;
	}

	/**
	 * This method sets the question of the students
	 * 
	 * @param questions of the student
	 */
	public void setQuestions(String questions) {
		this.questions = questions;
	}
}

/**
 * The Student class is responsible to keep the details of the students
 */
class Student {
	/**
	 * A String to keep track of the email id
	 */
	private String emailId;

	/**
	 * A Calendar type to keep the track of the ban date of the student
	 */
	private Calendar banDate;

	/**
	 * This method set the email id of the student
	 * 
	 * @param emailId
	 */
	public Student(String emailId) {
		this.emailId = emailId;
	}

	/**
	 * This method is used to get the email id of the student
	 * 
	 * @return emailId
	 */
	public String getEmailId() {
		return this.emailId;
	}

	/**
	 * sets the Ban date of the student
	 * 
	 * @param banDate
	 */
	public void setBanDate(Calendar banDate) {
		this.banDate = banDate;
	}

	/**
	 * This method gets the Banned date of the student
	 * 
	 * @return banDate
	 */
	public Calendar getBanDate() {
		return this.banDate;
	}
}
