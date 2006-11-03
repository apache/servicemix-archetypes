/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.servicemix.tooling;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.Properties;

import junit.framework.TestCase;

import org.apache.maven.cli.ConsoleDownloadMonitor;
import org.apache.maven.embedder.MavenEmbedder;
import org.apache.maven.embedder.MavenEmbedderConsoleLogger;
import org.apache.maven.embedder.MavenEmbedderLogger;
import org.apache.maven.embedder.PlexusLoggerAdapter;
import org.apache.maven.monitor.event.DefaultEventMonitor;
import org.apache.maven.monitor.event.EventMonitor;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.FileUtils;

public class ArchetypeTest extends TestCase {

    private File baseDir = new File(System.getProperty("basedir", ".")).getAbsoluteFile();

    private MavenEmbedder maven;
    private Properties sysProps = System.getProperties();
    private String version;
    
    protected void setUp() throws Exception {
        maven = new MavenEmbedder();
        maven.setOffline(true);
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        maven.setClassLoader(classLoader);
        MavenEmbedderLogger logger = new MavenEmbedderConsoleLogger();
        //logger.setThreshold(MavenEmbedderLogger.LEVEL_DEBUG);
        maven.setLogger(logger);
        maven.start();

        /*
        Field f = maven.getClass().getDeclaredField("wagonManager");
        f.setAccessible(true);
        WagonManager wagon = (WagonManager) f.get(maven);
        wagon.setOnline(false);
        */
        
        MavenProject project = maven.readProject(new File(baseDir, "pom.xml"));
        version = project.getVersion();
    }
    
    protected void tearDown() throws Exception {
        maven.stop();
        maven = null;
        System.gc();
    }
    
    public void testBindingComponent() throws Exception {
        testServiceMixArchetype("binding-component");
    }
    
    public void testEIPServiceUnit() throws Exception {
        testServiceMixArchetype("eip-service-unit");
    }
    
    public void testEmbeddedSimple() throws Exception {
        testServiceMixArchetype("embedded-simple");
    }
    
    public void testHttpConsumerServiceUnit() throws Exception {
        testServiceMixArchetype("http-consumer-service-unit");
    }
    
    public void testHttpProviderServiceUnit() throws Exception {
        testServiceMixArchetype("http-provider-service-unit");
    }
    
    public void testJmsConsumerServiceUnit() throws Exception {
        testServiceMixArchetype("jms-consumer-service-unit");
    }
    
    public void testJmsProviderServiceUnit() throws Exception {
        testServiceMixArchetype("jms-provider-service-unit");
    }
    
    public void testJsr181WsdlFirstServiceUnit() throws Exception {
        testServiceMixArchetype("jsr181-wsdl-first-service-unit");
    }
    
    public void testLwContainerServiceUnit() throws Exception {
        testServiceMixArchetype("lwcontainer-service-unit");
    }
    
    public void testServiceAssembly() throws Exception {
        testServiceMixArchetype("service-assembly");
    }
    
    public void testServiceEngine() throws Exception {
        testServiceMixArchetype("service-engine");
    }
    
    public void testServiceUnit() throws Exception {
        testServiceMixArchetype("service-unit");
    }
    
    public void testSharedLibrary() throws Exception {
        testServiceMixArchetype("shared-library");
    }
    
    protected void testServiceMixArchetype(String artifactId) throws Exception {
        testArchetype("org.apache.servicemix.tooling", "servicemix-" + artifactId, version);
    }
    
    protected void testArchetype(String groupId, String artifactId, String version) throws Exception {
        File targetDir = new File(baseDir, "target/archetypes/" + artifactId);
        FileUtils.deleteDirectory(targetDir);
        targetDir.mkdirs();
        EventMonitor eventMonitor = new DefaultEventMonitor(new PlexusLoggerAdapter(
                        new MavenEmbedderConsoleLogger()));
        Properties props = new Properties();
        props.setProperty("archetypeGroupId", groupId);
        props.setProperty("archetypeArtifactId", artifactId);
        props.setProperty("archetypeVersion", version);
        props.setProperty("groupId", "sample");
        props.setProperty("artifactId", "sample");
        props.setProperty("basedir", targetDir.getAbsolutePath());
        
        MavenProject parent = maven.readProject(getDefaultArchetypePom());
        System.setProperties((Properties) sysProps.clone());
        System.setProperty("user.dir", targetDir.getAbsolutePath());
        maven.execute(parent, 
                      Collections.singletonList("archetype:create"), 
                      eventMonitor, 
                      new ConsoleDownloadMonitor(), 
                      props, 
                      targetDir);

        System.setProperties((Properties) sysProps.clone());
        targetDir = new File(targetDir, "sample");
        System.setProperty("user.dir", targetDir.getAbsolutePath());
        System.setProperty("basedir", targetDir.getAbsolutePath());
        MavenProject prj = maven.readProject(new File(targetDir, "pom.xml"));
        maven.execute(prj, 
                      Collections.singletonList("package"), 
                      eventMonitor, 
                      new ConsoleDownloadMonitor(), 
                      new Properties(), 
                      targetDir);
    }

    private File getDefaultArchetypePom() throws IOException {
        URL archetypePom = getClass().getClassLoader().getResource("archetype-pom.xml");
        File pomFile = File.createTempFile("archetypePom", "xml");
        FileUtils.copyURLToFile(archetypePom, pomFile);
        return pomFile;
    }

}
