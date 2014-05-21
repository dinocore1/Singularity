package org.devsmart.singularity;


import com.google.inject.*;
import com.google.inject.name.Named;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.tooling.GlobalGraphOperations;

import java.io.File;
import java.util.Iterator;

public class DBTest {

    private static class TestModule extends AbstractModule {

        @Override
        protected void configure() {

        }

        @Provides @Named("homedir")
        public File provideBaseDir() {
            File baseDir = new File("testhome");
            if(!baseDir.exists()) {
                baseDir.mkdirs();
            }
            return baseDir;
        }

        @Provides @Named("file.db")
        public File provideDBFile(@Named("homedir") File homedir) {
            return new File(homedir, "graph.db");
        }

        @Provides @Singleton
        GraphDatabaseService provideDatabaseService(@Named("file.db") File graphdbfile) {
            String path = graphdbfile.getAbsolutePath();
            GraphDatabaseService retval = new GraphDatabaseFactory().newEmbeddedDatabase(path);
            return retval;
        }


    }

    private static Injector mInjector;

    @BeforeClass
    public static void setup() {
        mInjector = Guice.createInjector(new TestModule());
    }

    @Inject
    GraphDatabaseService mGraphDb;

    @Before
    public void doInject() {
        mInjector.injectMembers(this);
    }

    @Test
    public void dbTest() {


        try(Transaction tx = mGraphDb.beginTx()) {
            Iterator<Node> it = GlobalGraphOperations.at(mGraphDb).getAllNodes().iterator();
            while (it.hasNext()) {
                Node node = it.next();
                System.out.println(String.format("node: %s", node.getId()));
                for (String key : node.getPropertyKeys()) {
                    Object value = node.getProperty(key);
                    System.out.println(String.format("%s=%s", key, value));
                }

            }
        }


        /*
        Transaction tx = mGraphDb.beginTx();
        try {

            Node newNode = mGraphDb.createNode();
            newNode.setProperty("name", "Paul Soucy");


            tx.success();
        } finally {
            tx.close();
        }
        */

    }
}
