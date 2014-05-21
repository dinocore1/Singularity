package com.devsmart.singularity;


import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.schema.Schema;

public class Database {


    public static void firstTimeInit(GraphDatabaseService db) {
        try(Transaction tx = db.beginTx()){
            Schema schema = db.schema();
            schema.indexFor(DynamicLabel.label("Human"))
                    .on(Human.FULLNAME)
                    .create();
            tx.success();
        }
    }
}
