package com.ct.cql.entity;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


public class InstanceInfo {

	private String name;

    private boolean resolved;
    
    private CopyOnWriteArrayList<String> dependentObjects;
    
    public InstanceInfo(){
    	this.dependentObjects = new CopyOnWriteArrayList<String>();
    }
	public List<String> getDependentObjects() {
		return dependentObjects;
	}

	public void setDependentObjects(CopyOnWriteArrayList<String> dependentObjects) {
		this.dependentObjects = dependentObjects;
	}

	public boolean isResolved() {
		return resolved;
	}

	public void setResolved(boolean resolved) {
		this.resolved = resolved;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "InstanceInfo [name=" + name + ", resolved=" + resolved + "]";
	}

}
