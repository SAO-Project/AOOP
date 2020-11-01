/**
 * @author Alex Avila
 * @version 1.0
 * @since 9/28/20
 * <p>
 * Has the main attributes that a person would have.
 */
public abstract class Person {
	String firstName;
	String lastName;
	String dob;
	String address;
	String phone;
	
	/**
	 * Default constructor
	 */
	Person(){}
	
	/**
	 * Abstract class that has all the attributes of a person
	 * @param firstName First name of the person
	 * @param lastName Last name of the person
	 * @param dob Date of birth of the person
	 * @param address Address of the person contains exact location as String
	 * @param phone Phone number of the person store as a string.
	 */
	Person(String firstName, String lastName, String dob, String address, String phone){
		this.firstName = firstName;
		this.lastName = lastName;
		this.dob = dob;
		this.address = address;
		this.phone = phone;
	}
	
	/*-----------------------------------------------------------------------------------------------------------------
	                                            Getters
	 ----------------------------------------------------------------------------------------------------------------*/
	
	public String getFirstName() {
		return firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public String getDob() {
		return dob;
	}
	
	public String getAddress() {
		return address;
	}
	
	public String getPhone() {
		return phone;
	}
	
	public String getFullName(){
		return firstName + " " + lastName;
	}
	
	/*-----------------------------------------------------------------------------------------------------------------
	                                            Setters
	 ----------------------------------------------------------------------------------------------------------------*/
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public void setDob(String dob) {
		this.dob = dob;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public void setPhone(String phone) {
		this.phone = phone;
	}
}

