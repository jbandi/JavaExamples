package jbandi;
import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.*;
import org.eclipse.jgit.api.CreateBranchCommand.SetupUpstreamMode;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepository;

import org.junit.Before;
import org.junit.Test;

import javax.print.attribute.standard.DateTimeAtCompleted;

public class AppTest {

    private String localPath, remotePath, filename = "myfile";
    private Repository localRepo;
    private Git git;

    @Before
    public void init() throws IOException {
        localPath = "./jtest";
//        remotePath = "git@github.com:me/mytestrepo.git";
        localRepo = new FileRepository(localPath + "/.git");
        git = new Git(localRepo);
    }

    @Test
    public void testCreate() throws IOException {
        Repository newRepo = new FileRepository(localPath + "/.git");
        newRepo.create();
    }

//    @Test
//    public void testClone() throws Exception {
//        Git.cloneRepository()
//                .setURI(remotePath)
//                .setDirectory(new File(localPath))
//                .call();
//    }

    @Test
    public void testAdd() throws Exception {
        File myfile = new File(localPath + "/" + filename);
        if (!myfile.exists())
            myfile.createNewFile();
        FileUtils.writeStringToFile(myfile, new Date().toString());
        git.add()
                .addFilepattern(filename)
                .call();
    }

    @Test
    public void testCommit() throws Exception {
        git.commit()
                .setMessage("Changed myfile")
                .call();
    }

    @Test
    public void testHistory() throws Exception {
        for (RevCommit revCommit : git.log().call()) {
            System.out.println(revCommit.getFullMessage());
            Ref ref = git.checkout().setStartPoint(revCommit).addPath(filename).call();
            FileUtils.moveFile(new File(localPath + "/" + filename), new File(localPath + "/" + filename + "-" + revCommit.getId().name()));

        }
    }

//    @Test
//    public void testPush() throws Exception {
//        git.push()
//                .call();
//    }

//    @Test
//    public void testTrackMaster() throws Exception {
//        git.branchCreate()
//                .setName("master")
//                .setUpstreamMode(SetupUpstreamMode.SET_UPSTREAM)
//                .setStartPoint("origin/master")
//                .setForce(true)
//                .call();
//    }
//
//    @Test
//    public void testPull() throws Exception {
//        git.pull()
//                .call();
//    }
}