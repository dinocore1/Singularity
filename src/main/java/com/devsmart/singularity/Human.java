package com.devsmart.singularity;


import org.neo4j.graphdb.Node;

public class Human {

    public static final String FULLNAME = "fullname";

    private final Node mNode;

    public Human(Node node){
        mNode = node;
    }

    public String getFullname() {
        return (String) mNode.getProperty(FULLNAME);
    }

    @Override
    public String toString() {
        return String.format("Human[%s]", getFullname());
    }

    @Override
    public int hashCode() {
        return mNode.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        boolean retval = false;
        if(obj instanceof Human){
            retval = mNode.equals(((Human) obj).mNode);
        }
        return retval;
    }
}
