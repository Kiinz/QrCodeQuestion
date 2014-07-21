package at.klu.qrcodequest;

public class Node {
	
	private int id;
	private int questPk;
	private int active;
	private int sequence;
	private int dtRegistration;
	private String name;
	private String description;
	private String location;
	
	
	public Node(int id, int questPk, int active, int sequence,
			int registration, String name, String description, String location) {
		super();
		this.id = id;
		this.questPk = questPk;
		this.active = active;
		this.sequence = sequence;
		this.dtRegistration = registration;
		this.name = name;
		this.description = description;
		this.location = location;
	}
	public Node(int questPk, int active, int sequence,
			int registration, String name, String description, String location) {
		super();
		this.questPk = questPk;
		this.active = active;
		this.sequence = sequence;
		this.dtRegistration = registration;
		this.name = name;
		this.description = description;
		this.location = location;
	}
	
	public Node() {
		
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getQuestPk() {
		return questPk;
	}
	public void setQuestPk(int questPk) {
		this.questPk = questPk;
	}
	public int getActive() {
		return active;
	}
	public void setActive(int active) {
		this.active = active;
	}
	public int getSequence() {
		return sequence;
	}
	public void setSequence(int sequence) {
		this.sequence = sequence;
	}
	public int getRegistration() {
		return dtRegistration;
	}
	public void setRegistration(int registration) {
		this.dtRegistration = registration;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	@Override
	public String toString() {
		return "Node [id=" + id + ", questPk=" + questPk + ", active=" + active
				+ ", sequence=" + sequence + ", dtRegistration="
				+ dtRegistration + ", name=" + name + ", description="
				+ description + ", location=" + location + "]";
	}
	
	
	
	

}
