/**
 * Representation of a graph vertex
 */
public class Vertex implements Comparable<Vertex> {
	private final String label; // label attached to this vertex
	private int cost;
	private boolean known;
	private Vertex path;
	/**
	 * Construct a new vertex
	 * @param label the label attached to this vertex
	 */
	public Vertex(String label) {
		if(label == null)
			throw new IllegalArgumentException("null");
		this.label = label;
		cost = 0;
		//known = false;
		//path = null;
	}
	
	public Vertex(String label, int cost, Vertex path) {
		if(label == null)
			throw new IllegalArgumentException("null");
		this.label = label;
		//this.cost = cost;
		//known = false;
		//this.path = path;
	}
	
	//sets the vertex's cost
	public void setCost(int cost) {
		this.cost = cost;
	}
	
	public int getCost() {
		int output = this.cost;
		return output;
	}
	/*
	//sets the vertex's known status
	public void setKnown(boolean known) {
		this.known = known;
	}
	
	public boolean getKnown() {
		boolean output = this.known;
		return output;
	}
	
	public void setPath(Vertex in) {
		Vertex inCopy = new Vertex(in.getLabel());
		this.path = inCopy;
	}
	
	public Vertex getPath() {
		Vertex outCopy = new Vertex(path.getLabel(), path.getCost(), path);
		return outCopy;
	}*/

	/**
	 * Get a vertex label
	 * @return the label attached to this vertex
	 */
	public String getLabel() {
		return label;
	}
	
	/**
	 * A string representation of this object
	 * @return the label attached to this vertex
	 */
	public String toString() {
		return label;
	}

	//auto-generated: hashes on label
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((label == null) ? 0 : label.hashCode());
		return result;
	}

	//auto-generated: compares labels
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Vertex other = (Vertex) obj;
		if (label == null) {
                    return other.label == null;
		} else {
		    return label.equals(other.label);
		}
	}
	
	public int compareTo(Vertex input) {
		//int internalCost = this.cost;
		//int inputCost = input.getCost();
		return Integer.compare(this.getCost(), input.getCost());
		
	}


}
