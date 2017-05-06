package ru.croc.frpo.Console;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

import junit.framework.TestCase;

public class DirectoryOptTest extends TestCase {
	
	public void tearDown(){
	System.setProperty("user.dir", "C:\\Users\\Елизавета\\workspaceJava\\Console");
	}
	
	DirectoryOpt directory = new DirectoryOpt();
	
	@Test
	public void testMkdir(){
		Path path = Paths.get("C:\\Users\\Елизавета\\Desktop\\testing\\new");
		directory.mkdir(path);
		File file = path.toFile();
		assertTrue(file.exists());
		assertFalse(file.isFile());
		if (file.exists()){
			try {
				Files.delete(path);
			} catch (IOException e) {}
		}
	}
	
	@Test
	public void testMkDirExhist(){
		Path path = Paths.get("C:\\Users");
		assertFalse(directory.mkdir(path));
	}
	
	@Test
	public void testMkdirShort(){
		Path path = Paths.get("new");
		directory.mkdir(path);
		File file = path.toFile();
		assertTrue(file.exists());
		assertFalse(file.isFile());
		if (file.exists()){
			try {
				Files.delete(path);
			} catch (IOException e) {}
		}
	}
	
	@Test 
	public void testListFiles(){
		Path path = Paths.get("C:\\Users\\Елизавета\\Downloads");
		File[] list = directory.listFiles(path);
		String filename = "C:\\Users\\Елизавета\\Downloads\\Diplom_1.pdf";
		boolean flag = false;
//		directory.printFiles(list);
		for (File f :list){
			if (f.toString().equals(filename)){
				flag = true;
			}
		}
		assertTrue(flag);
	}
	
	@Test
	public void testListFilesVoid(){
		File[] list = directory.listFiles();
		String filename = "C:\\Users\\Елизавета\\workspaceJava\\Console\\pom.xml";
		boolean flag = false;
	//	directory.printFiles(list);
		for (File f :list){
			if (f.toString().equals(filename)){
				flag = true;
			}
		}
		assertTrue(flag);
	}
	
	@Test 
	public void testChangeDir(){
		Path path = Paths.get("C:\\Users\\Елизавета\\Desktop");
		directory.changeDir(path);
		assertEquals(path.toString(), System.getProperty("user.dir"));
	}
	
	@Test
	public void testChangeDirNoFile(){
		Path path = Paths.get("Это не те дроиды, которых вы ищете");
		assertFalse(directory.changeDir(path));
	}
	
	@Test
	public void testChangeDirYesFile(){
		Path path = Paths.get("Это не те дроиды, которых вы ищете");
		directory.mkdir(path);
		assertTrue(directory.changeDir(path));
		File file = path.toFile();
		if (file.exists()){
			try {
				Files.delete(path);
			} catch (IOException e) {}
		}
	}
	
	@Test 
	public void testListFilesNE(){
		Path path = Paths.get("Это не те дроиды, которых вы ищете");
		File[] list = directory.listFiles(path.toAbsolutePath());
		assertNull(list);
	}
	
	@Test
	public void testGoUp(){
		Path path = Paths.get("C:\\Users\\Елизавета\\Desktop");
		directory.changeDir(path);
		directory.goUp();
		assertEquals(Paths.get("C:\\Users\\Елизавета").toString(), System.getProperty("user.dir"));
	}
	
	@Test
	public void testGoUpRoot(){
		Path path = Paths.get("C:\\");
		directory.changeDir(path);
		assertFalse(directory.goUp());
	}



}
