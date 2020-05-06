package GUI;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Queue;

import javax.swing.JFrame;
import javax.swing.JLabel;

import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.junit.jupiter.api.Test;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class Junit_testing {
	
	SEC_Part3 test = new SEC_Part3();
	
	Queue<Reservation> test_globalqueue = new LinkedList<Reservation>();
	Queue<Reservation> test_empty_globalqueue = new LinkedList<Reservation>();
	
	
	@Before
    public void init() {
		
		Student stu1 = new Student("stu1@buffalo.edu");
		Student stu2 = new Student("stu2@buffalo.edu");
		Student stu3 = new Student("stu3@buffalo.edu");
		Student stu4 = new Student("stu4@buffalo.edu");
		
		
		Calendar cal = Calendar.getInstance();
		
		
		Reservation res1 = new Reservation(stu1, cal);
		Reservation res2 = new Reservation(stu2, cal);
		Reservation res3 = new Reservation(stu3, cal);
		Reservation res4 = new Reservation(stu4, cal);
		

		test_globalqueue.add(res1);
		test_globalqueue.add(res2);
		test_globalqueue.add(res3);
		test_globalqueue.add(res4);
    }

	
	
	/**** This method Tests whether the  banned  student is not present in the queue and also 
	      his ban Date attribute is updated to current date and the queue is Updated accordingly ****/
	
	@Test
	void test__Student_is_10Minutes_or_more_late() {
		
		init();
		Queue<Reservation>  clone = new LinkedList<>(test_globalqueue) ;
		
		Reservation ban_reservation = clone.peek();
		Student ban_student = ban_reservation.getStudent();
		
		Calendar cal = test_globalqueue.poll().getStudent().getBanDate();
		
		//*** Testing whether initially the ban date of the banned student is null ***//
		
		Assert.assertTrue(cal==null);
		test.banStudent(clone);
		
		//*** Checking if the banned student is not present in the updated queue ***//
		
		Assert.assertFalse(clone.contains(ban_reservation));
		
		Iterator<Reservation> itr = clone.iterator();
		
		while(itr.hasNext()) {
			assertNotEquals(itr.next().getStudent().getEmailId(),ban_student.getEmailId());
		}
		
		
		//*** Testing whether the Ban date  of the banned student is same as the current date ***//
		
		cal = ban_student.getBanDate();
		
		Assert.assertTrue((cal.get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR)) && (cal.get(Calendar.MONTH) == Calendar.getInstance().get(Calendar.MONTH)) && (cal.get(Calendar.DATE) == Calendar.getInstance().get(Calendar.DATE)) );
		
		//*** Testing whether the updated queue is same as the original queue (minus) the banned reservation ****/
		
		assertEquals(clone,test_globalqueue);
		
		/**########## Testing whether each of the individuals and their attributes are matching. #######***/
		
		Iterator<Reservation> itr_clone = clone.iterator();
		Iterator<Reservation> itr_test_globalqueue = test_globalqueue.iterator();
		
		//*** hasNext() returns true if the queue has more elements ***//
		
		
		while (itr_test_globalqueue.hasNext()||itr_clone.hasNext()) 
		{
			Reservation current_reservation_clone = itr_clone.next();
			
			Reservation current_reservation_test_globalqueue = itr_test_globalqueue.next();
			
			//*** Testing whether the current reservation email id is matching ***//
			Assert.assertEquals(current_reservation_test_globalqueue.getStudent().getEmailId(),current_reservation_clone.getStudent().getEmailId());
			
			//*** Testing whether the current reservation email id is matching ***//
			Assert.assertEquals(current_reservation_test_globalqueue.getQuestions(),current_reservation_clone.getQuestions());

			//*** Testing whether the current reservation  ReservationTime  is matching ***//
			Assert.assertEquals(current_reservation_test_globalqueue.getReservationTime(),current_reservation_clone.getReservationTime());
		
			
		
			
		}
	}
	
	/**** This Method tests whether the Student who is under 10 minutes late is put at the end of
	 	  the queue  and also whether the Queue is updated accordingly ****/
	
	
	@Test
	void test_Student_is_Under_10Minutes_Late() {
		init();
		
		Queue<Reservation>  clone = new LinkedList<>(test_globalqueue) ;
		test.addReservationToEndOfQueue(clone);
		
		test_globalqueue.add(test_globalqueue.poll());
		
		//*** Testing whether the student has been removed from the queue and added to the end of the list ***//
		
		assertEquals(clone,test_globalqueue);
		
		/**########## Testing whether each of the individuals and their attributes are matching. #######***/
		
				Iterator<Reservation> itr_clone = clone.iterator();
				Iterator<Reservation> itr_test_globalqueue = test_globalqueue.iterator();
				
				//*** hasNext() returns true if the queue has more elements ***//
				
				
				while (itr_test_globalqueue.hasNext()||itr_clone.hasNext()) 
				{
					Reservation current_reservation_clone = itr_clone.next();
					
					Reservation current_reservation_test_globalqueue = itr_test_globalqueue.next();
					
					//*** Testing whether the current reservation email id is matching ***//
					Assert.assertEquals(current_reservation_test_globalqueue.getStudent().getEmailId(),current_reservation_clone.getStudent().getEmailId());
					
					//*** Testing whether the current reservation email id is matching ***//
					Assert.assertEquals(current_reservation_test_globalqueue.getQuestions(),current_reservation_clone.getQuestions());

					//*** Testing whether the current reservation  ReservationTime  is matching ***//
					Assert.assertEquals(current_reservation_test_globalqueue.getReservationTime(),current_reservation_clone.getReservationTime());
				
					
				
					
				}
		
	}
	
	/**** This method Tests whether the Global Queue Variable is getting updated ****/
	
	@Test
	void test_updateReservationQueue() {
		init();
		test.updateReservationQueue(test_globalqueue);
		
		//*** Testing whether the Global reservation queue variabe is getting updated or not ***//
		
		assertEquals(test.globalReservationQueue, test_globalqueue);
		
		/**########## Testing whether each of the individuals and their attributes are matching. #######***/
		
		Iterator<Reservation> itr_clone = test.globalReservationQueue.iterator();
		Iterator<Reservation> itr_test_globalqueue = test_globalqueue.iterator();
		
		//*** hasNext() returns true if the queue has more elements ***//
		
		
		while (itr_test_globalqueue.hasNext()||itr_clone.hasNext()) 
		{
			Reservation current_reservation_clone = itr_clone.next();
			
			Reservation current_reservation_test_globalqueue = itr_test_globalqueue.next();
			
			//*** Testing whether the current reservation email id is matching ***//
			Assert.assertEquals(current_reservation_test_globalqueue.getStudent().getEmailId(),current_reservation_clone.getStudent().getEmailId());
			
			//*** Testing whether the current reservation email id is matching ***//
			Assert.assertEquals(current_reservation_test_globalqueue.getQuestions(),current_reservation_clone.getQuestions());

			//*** Testing whether the current reservation  ReservationTime  is matching ***//
			Assert.assertEquals(current_reservation_test_globalqueue.getReservationTime(),current_reservation_clone.getReservationTime());
		
			
		
			
		}
		
		
	}
	
	
	
	
	
	
	
	
	
	//########################################################################################//
	//########################################################################################//
	//########################################################################################//
	//########################################################################################//
	
	 /*
     * ========================================================================================
     * ================== EXTRA TEST CASES FOR STUDENT PRESENT SCENARIO =======================
     * ========================================================================================
     */
	
	//########################################################################################//
	//########################################################################################//
	//########################################################################################//
	//########################################################################################//
	
	
	
	
	
	
	
	
	
	
	/**** This method Tests whether the student is removed from the Queue when he's present 
	      and the Queue is updated accordingly ****/
	
	@Test
	void test_markStudentPresent() {
		init();
		Queue<Reservation>  clone = new LinkedList<>(test_globalqueue) ;
		Reservation clone_curr  = clone.peek();
		test.markStudentPresent(clone);
		
		//***Testing whether the current reservation is matching ***//
		
		assertEquals(clone_curr,test_globalqueue.poll());
		
		//*** Testing whether the current reservation is being taken out of the queue and the updated queues are matching ***//
		
		assertEquals(clone,test_globalqueue);
		
		/**########## Testing whether each of the individuals and attributes are matching. #######***/
		
		Iterator<Reservation> itr_clone = clone.iterator();
		Iterator<Reservation> itr_test_globalqueue = test_globalqueue.iterator();
		
		//*** hasNext() returns true if the queue has more elements ***//
		
		
		while (itr_test_globalqueue.hasNext()||itr_clone.hasNext()) 
		{
			Reservation current_reservation_clone = itr_clone.next();
			
			Reservation current_reservation_test_globalqueue = itr_test_globalqueue.next();
			
			//*** Testing whether the current reservation email id is matching ***//
			Assert.assertEquals(current_reservation_test_globalqueue.getStudent().getEmailId(),current_reservation_clone.getStudent().getEmailId());
			
			//Testing whether the current reservation email id is matching
			Assert.assertEquals(current_reservation_test_globalqueue.getQuestions(),current_reservation_clone.getQuestions());

			//Testing whether the current reservation  ReservationTime  is matching
			Assert.assertEquals(current_reservation_test_globalqueue.getReservationTime(),current_reservation_clone.getReservationTime());
		
			
		
			
		}
	}

	
	/** This Method Tests whether the appropriate Message text is getting printed in the JFrame **/
	
	@Test
	void test_displayCurrentStatusOfQueue() {
		JFrame frame = new JFrame("Reservation Queue");
		frame.setBounds(100, 100, 1000, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		// Label for the Initial Reservation Queue display
		JLabel lblNewLabel_1 = new JLabel("Current Reservation Queue");
		lblNewLabel_1.setBounds(20, 45, 190, 20);
		frame.getContentPane().add(lblNewLabel_1);
		frame.setVisible(false);
		
		test.globalReservationQueue = test_empty_globalqueue;
		
		test.displayCurrentStatusOfQueue(frame);
		
		//Testing for and  empty reservation queue and asserting whether the "Reservation Queue Empty" Message is being returned correctly to the textArea ///
		
		assertEquals("There were no appointments on the reservation Queue !" , test.textArea.getText());
		
		init();
		test.globalReservationQueue = test_globalqueue;
		test.displayCurrentStatusOfQueue(frame);
		
		StringBuilder sb = new StringBuilder();
		while (!test.globalReservationQueue.isEmpty()) {
			sb.append(test.globalReservationQueue.poll().getStudent().getEmailId() + ",  ");
		}
		
		//Testing for an non-empty reservation queue and asserting whether the output queue Message is being returned correctly to the textArea ///
		System.out.println(sb.toString());
		assertEquals(sb.toString(),test.textArea.getText());
		test.textArea.getText();
		
	}
	
	/** This Method tests whether the Reservation & Student objects are getting created in the  required way **/
	
	@Test
	void test_getReservationQueue() {
		
		Queue<Reservation> output = test.getReservationQueue(4);
		
		/// Checking the size of the queue //
		
		assertEquals(4,output.size());
		
				Iterator<Reservation> itr = output.iterator();
				int i =1;
				// hasNext() returns true if the queue has more elements
				int count_questions=0;
				while (itr.hasNext()) 
				{
					Reservation current_res = itr.next();
					
					// Testing if email id is non null value
					Assert.assertTrue(current_res.getStudent().getEmailId()!=null);
					
					// Testing if  email id is as expected //***** check for only the domain name **** ///
					Assert.assertTrue(current_res.getStudent().getEmailId().contains("@buffalo.edu"));

					if(current_res.getQuestions()!=null)
					if (current_res.getQuestions().equals("Questions for the Student : \n Q.1. First Question \n Q.2. SecondQuestion")){
						count_questions++;
					}
					
					//Testing if the TIme instance is not null
					Assert.assertTrue(current_res.getReservationTime()!=null);
					
					// Testing for reservation time that is chosen 
					//randomly from thecurrent time, 5 minutes before the current time,
					// or 11 minutes before the current time(chosen randomly).
					
					long diffInMinutes = (long)(Calendar.getInstance().getTimeInMillis() - current_res.getReservationTime().getTimeInMillis())/(1000*60);
					
					Assert.assertTrue((diffInMinutes==11) || (diffInMinutes==5) || (diffInMinutes==0));
					

					
					i++;
				}
				// Testing if atleast half of the reservations  have a sample question
				System.out.println(count_questions + " *** " + output.size());
				Assert.assertTrue(count_questions>=output.size()/2);
		
		
		
		
		
		
	}

}
