/*Graham Arnold
 * 2/22/17
 * CSE373
 * 
 * 
 */
import java.util.*;

/**
 * A representation of a graph.
 * Assumes that we do not have negative cost edges in the graph.
 * allows the user to compute the shortest path using
 * Dijkstra's algorithm
 */
public class MyGraph implements Graph {
   
	private Collection<Vertex> internalV;
	private Collection<Edge> internalE;
	private Map<Vertex, ArrayList<Edge>> inEdgesMap;
	private Map<Vertex, ArrayList<Edge>> outEdgesMap;
	private PriorityQueue<Vertex> checkV;
	private Map<Vertex, Integer> costMap;
	private Map<Vertex, Vertex> pathMap;
	
	/*
	 * Private helper function to perform error checking on
	 * constructor inputs. Proper exceptions are thrown if 
	 * an error is detected
	 * @param v: input collection of vertex objects
	 * @param e: input collection of edge objects
	 */
	private void errorChecking(Collection<Vertex> v, Collection<Edge> e) {
		for(Edge currE : e) {
    		if(!v.contains(currE.getSource()) || !v.contains(currE.getDestination())) {
    			throw new IllegalArgumentException("Graph does not contain one or both vertices in specified edge");
    		}
    		if(currE.getWeight() < 0) {
    			throw new IllegalArgumentException("Edge weight cannot be negative");
    		}
    		//checking if there exists duplicate edges with different weight
    		//previously checked edges are added to a set to improve efficiency
    		//by avoiding looping over edges that have already been compared
    		//current edge is before the loop since it does not need to be checked
    		//against itself
    		Set<Edge> checkedEdges = new HashSet<Edge>();
    		checkedEdges.add(currE);
    		for(Edge currE2 : e) {
    			if(!checkedEdges.contains(currE2)) {
    				if(currE.getSource().equals(currE2.getSource())
        					&& currE.getDestination().equals(currE2.getDestination())
        						&& (currE.getWeight() != currE2.getWeight())) {
        				throw new IllegalArgumentException("Multiple edges found with different weights");
    				}	
    			}
    		}
    	}
	}
    /**
     * Creates a MyGraph object with the given collection of vertices
     * and the given collection of edges.
     * @param v a collection of the vertices in this graph
     * @param e a collection of the edges in this graph
     */
    public MyGraph(Collection<Vertex> v, Collection<Edge> e) {
    	errorChecking(v,e);   
    	internalV = new ArrayList<Vertex>();
    	internalE = new ArrayList<Edge>();
    	inEdgesMap = new HashMap<Vertex, ArrayList<Edge>>();
    	outEdgesMap = new HashMap<Vertex, ArrayList<Edge>>();
    	checkV = new PriorityQueue<Vertex>();
    	costMap = new HashMap<Vertex, Integer>();
    	pathMap = new HashMap<Vertex, Vertex>();
    	//deep copying of constructor inputs
    	for(Vertex currV : v) {
    		Vertex currVInternal = new Vertex(currV.getLabel());
    		internalV.add(currVInternal);
    	}
    	for(Edge currE : e) {
    		Edge currEInternal = new Edge(currE.getSource(), currE.getDestination(), currE.getWeight());
    		internalE.add(currEInternal);
    	}
    	//mapping vertexes to lists of their
    	//inbound and outbound edges
    	//to create an adjacency list
    	for(Vertex currV : internalV) {
    		ArrayList<Edge> inEdges = new ArrayList<Edge>();
    		ArrayList<Edge> outEdges = new ArrayList<Edge>();	
    		for(Edge currE : internalE) {
    			if(currE.getSource().equals(currV)) {
    				//handles ignoring duplicates
    				if(!outEdges.contains(currE)) {
    					outEdges.add(currE);
    				}
    			}
    			if(currE.getDestination().equals(currV)) {
    				//handles ignoring duplicates
    				if(!inEdges.contains(currE)) {
    					inEdges.add(currE);
    				}
    			}
    		}
    		inEdgesMap.put(currV, inEdges);
    		outEdgesMap.put(currV, outEdges);
    	}
    	
    }

    /** 
     * Return the collection of vertices of this graph
     * @return the vertices as a collection (which is anything iterable)
     */
    public Collection<Vertex> vertices() {
    	//deep copying output object
    	ArrayList<Vertex> exportV = new ArrayList<Vertex>();
    	for(Vertex currV : internalV) {
    		exportV.add(currV);
    	}
    	return exportV;
    }

    /** 
     * Return the collection of edges of this graph
     * @return the edges as a collection (which is anything iterable)
     */
    public Collection<Edge> edges() {
    	//deep copying output object
    	ArrayList<Edge> exportE = new ArrayList<Edge>();
    	for(Edge currE : internalE) {
    		exportE.add(currE);
    	}
    	return exportE;
    }

    /**
     * Return a collection of vertices adjacent to a given vertex v.
     *   i.e., the set of all vertices w where edges v -> w exist in the graph.
     * Return an empty collection if there are no adjacent vertices.
     * @param v one of the vertices in the graph
     * @return an iterable collection of vertices adjacent to v in the graph
     * @throws IllegalArgumentException if v does not exist.
     */
    public Collection<Vertex> adjacentVertices(Vertex v) {
    	if(v == null) {
    		throw new IllegalArgumentException("Input vertex does not exist");
    	}
    	Collection<Vertex> exportV = new ArrayList<Vertex>();
    	//deep copying input Vertex object
    	Vertex vCopy = new Vertex(v.getLabel());
    	ArrayList<Edge> currEdges = outEdgesMap.get(vCopy);
    	for(Edge currE : currEdges) {
    		exportV.add(currE.getDestination());
    	}
    	return exportV;
    }

    /**
     * Test whether vertex b is adjacent to vertex a (i.e. a -> b) in a directed graph.
     * Assumes that we do not have negative cost edges in the graph.
     * @param a one vertex
     * @param b another vertex
     * @return cost of edge if there is a directed edge from a to b in the graph, 
     * return -1 otherwise.
     * @throws IllegalArgumentException if a or b do not exist.
     */
    public int edgeCost(Vertex a, Vertex b) {
    	//deep copying inputs
    	Vertex aCopy = new Vertex(a.getLabel());
    	Vertex bCopy = new Vertex(b.getLabel());
    	if(!internalV.contains(aCopy) || !internalV.contains(bCopy)) {
    		throw new IllegalArgumentException("One or both vertices provided do not exist");
    	}
    	ArrayList<Edge> aOutEdges = outEdgesMap.get(aCopy);
    	for (Edge aOutEdge : aOutEdges) {
    		if(aOutEdge.getDestination().equals(bCopy)) {
    			return aOutEdge.getWeight();
    		} 
    	}
    	return -1;
    }
    
    /**
     * Returns the shortest path from a to b in the graph, or null if there is
     * no such path.  Assumes all edge weights are nonnegative.
     * Uses Dijkstra's algorithm.
     * @param a the starting vertex
     * @param b the destination vertex
     * @return a Path where the vertices indicate the path from a to b in order
     *   and contains a (first) and b (last) and the cost is the cost of 
     *   the path. Returns null if b is not reachable from a.
     * @throws IllegalArgumentException if a or b does not exist.
     */
    public Path shortestPath(Vertex a, Vertex b) {
    	if (!internalV.contains(a) || !internalV.contains(b)) {
    		throw new IllegalArgumentException();
    	}
    	//deep copying inputs
    	Vertex aCopy = new Vertex(a.getLabel());
    	Vertex bCopy = new Vertex(b.getLabel());
    	Set<Vertex> knownSet = new HashSet<Vertex>();
    	//checking to see if a and b are the same
    	if(aCopy.equals(bCopy)) {
    		List<Vertex> output = new ArrayList<Vertex>();
    		output.add(aCopy);
    		return new Path(output, 0);
    	}
    	initializeVertices(aCopy);
    	//Performs Dijsktras algorithm starting at input source
    	while(!checkV.isEmpty()) {
    		Vertex currV = checkV.remove();
    		knownSet.add(currV);
    		Collection<Vertex> adjV = adjacentVertices(currV);
    		for (Vertex u : adjV) {
				int currEdgeCost = edgeCost(currV, u);
				int c1 = costMap.get(currV) + currEdgeCost;
				
				int c2 = costMap.get(u);
				if(c1 < c2) {
					costMap.put(u, c1);
					u.setCost(c1);
					pathMap.put(u, currV);
					checkV.add(u);
				}
    		}
    	}
    	return postProcessing(aCopy, bCopy, knownSet);
    }
    
    //private helper method to initialize the vertices
    //in the graph in preparation for Dijkstra's algorithm
    //@params: source vertex
    private void initializeVertices(Vertex aCopy) {
    	//setting all vertex costs to max value except source vertex
    	//adds that source vertex to the priority queue to get started
    	for (Vertex v : internalV) {
    		//if Vertex a is found, makes its cost 0 
    		if(v.equals(aCopy)) {
    			costMap.put(v, 0);
    			checkV.add(v);
    			
    		} else {
    			costMap.put(v, Integer.MAX_VALUE);
    		}
    	}
    }
    
    //private helper method to process the vertices after Dijkstra's Algorithm
    //this method reconstructs the the path and the pathCost
    //@params: start and end vertices and the set of known vertices
    //@return: Path object containing the shortest path and the cost of that path
    private Path postProcessing(Vertex aCopy, Vertex bCopy, Set<Vertex> knownSet) {
    	int pathCost = 0;
    	if(!knownSet.contains(bCopy)) {
    		return null;
    	} else {
    		pathCost = costMap.get(bCopy);
    		Vertex backPath = pathMap.get(bCopy);
    		Stack<Vertex> reorderStack = new Stack<Vertex>();
    		reorderStack.push(bCopy);
    		while(!backPath.equals(aCopy)) {
    			reorderStack.push(backPath);
    			backPath = pathMap.get(backPath);
    		}
    		reorderStack.push(aCopy);
    		List<Vertex> pathList = new ArrayList<Vertex>();
    		while(!reorderStack.isEmpty()) {
    			Vertex currV = reorderStack.pop();
    			pathList.add(currV);
    		}
    		//deep copying output
    		return new Path(pathList, pathCost);
    	}
    }
}
