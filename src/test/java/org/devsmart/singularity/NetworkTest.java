package org.devsmart.singularity;

import com.google.inject.*;
import com.google.inject.name.Named;
import net.jxta.discovery.DiscoveryService;
import net.jxta.document.AdvertisementFactory;
import net.jxta.document.Document;
import net.jxta.document.MimeMediaType;
import net.jxta.id.IDFactory;
import net.jxta.peergroup.PeerGroup;
import net.jxta.platform.ModuleClassID;
import net.jxta.platform.NetworkManager;
import net.jxta.protocol.*;
import net.jxta.resolver.ResolverService;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import java.io.File;
import java.io.IOException;

public class NetworkTest {

    private static class TestModule extends AbstractModule {

        @Override
        protected void configure() {

        }

        @Provides
        @Named("homedir")
        public File provideBaseDir() {
            File baseDir = new File("testhome");
            if(!baseDir.exists()) {
                baseDir.mkdirs();
            }
            return baseDir;
        }

        @Provides @Named("dir.p2p")
        public File provideP2PFile(@Named("homedir") File homedir) {
            return new File(homedir, "p2p");
        }

        @Provides @Named("dir.db")
        public File provideDBFile(@Named("homedir") File homedir) {
            return new File(homedir, "db");
        }

        @Provides @Singleton
        GraphDatabaseService provideDatabaseService(@Named("dir.db") File graphdbfile) {
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

    @Before
    public void inject(){
        mInjector.injectMembers(this);
    }

    @Inject @Named("dir.p2p")
    File p2pdir;

    @Test
    public void firstTest() throws Exception {

        NetworkManager manager = new NetworkManager(NetworkManager.ConfigMode.ADHOC,
                "TestClient", p2pdir.toURI());

        manager.startNetwork();

        PeerGroup netpeergroup = manager.getNetPeerGroup();
        DiscoveryService discovery = netpeergroup.getDiscoveryService();

        ModuleClassAdvertisement mcadv = (ModuleClassAdvertisement) AdvertisementFactory.newAdvertisement(ModuleClassAdvertisement.getAdvertisementType());
        mcadv.setName("devsmart:test");
        mcadv.setDescription("this is a test");
        ModuleClassID mcID = IDFactory.newModuleClassID();
        mcadv.setModuleClassID(mcID);

        discovery.publish(mcadv);
        discovery.remotePublish(mcadv);


        ModuleSpecAdvertisement moduleSpecAdvertisement = (ModuleSpecAdvertisement)AdvertisementFactory.newAdvertisement(ModuleSpecAdvertisement.getAdvertisementType());
        moduleSpecAdvertisement.setName("devsmart:test");
        moduleSpecAdvertisement.setVersion("Version 1.0");
        moduleSpecAdvertisement.setModuleSpecID(IDFactory.newModuleSpecID(mcID));

        /*
        PeerGroupAdvertisement peerGroupAdv = (PeerGroupAdvertisement)AdvertisementFactory.newAdvertisement(PeerGroupAdvertisement.getAdvertisementType());
        peerGroupAdv.setPeerGroupID(IDFactory.newPeerGroupID());
        peerGroupAdv.setName("Singularity");
        peerGroupAdv.setDescription("This is a test");
        peerGroupAdv.setModuleSpecID(IDFactory.newModuleSpecID());
        netpeergroup.newGroup(peerGroupAdv);
        */




    }
}
