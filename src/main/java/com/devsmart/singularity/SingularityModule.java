package com.devsmart.singularity;


import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import java.io.File;

public class SingularityModule extends AbstractModule {

    @Override
    protected void configure() {

    }

    @Provides @Named("dir.home")
    public File provideBaseDir() {
        File homedir = new File(System.getProperty("user.home"));
        File baseDir = new File(homedir, ".singularity");
        if(!baseDir.exists()) {
            baseDir.mkdirs();
        }
        return baseDir;
    }

    @Provides @Named("dir.p2p")
    public File provideP2PFile(@Named("dir.home") File homedir) {
        return new File(homedir, "p2p");
    }

    @Provides @Named("dir.db")
    public File provideDBFile(@Named("dir.home") File homedir) {
        return new File(homedir, "db");
    }

    @Provides @Singleton
    GraphDatabaseService provideDatabaseService(@Named("dir.db") File graphdbfile) {
        String path = graphdbfile.getAbsolutePath();
        GraphDatabaseService retval = new GraphDatabaseFactory().newEmbeddedDatabase(path);
        return retval;
    }
}
