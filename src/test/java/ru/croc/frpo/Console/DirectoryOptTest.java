package ru.croc.frpo.Console;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.Test;

import junit.framework.TestCase;

public class DirectoryOptTest extends TestCase {
	
	private Path absPath(Path path) {
		if (!path.isAbsolute()){
			String s = System.getProperty("user.dir") + "\\" + path.toString();
			path = Paths.get(s);
		}
		return path;
	}
	public void setUp(){
		System.setProperty("user.dir.old", System.getProperty("user.dir"));
	}
	
	
	public void tearDown(){
		System.setProperty("user.dir", System.getProperty("user.dir.old"));
	}
	

	@Test
	public void testMkdirLong() throws InvalidPathException, IOException{
		String path = "C:\\Users\\Елизавета\\Desktop\\testing";
		int code = DirectoryOpt.mkdir(path);
		assertEquals(1,code);
	}
	
	
	@Test
	public void testMkDirExist() throws InvalidPathException, IOException{
		System.setProperty("user.dir", "C:\\");
		String path = "Users";
		assertEquals(2, DirectoryOpt.mkdir(path));
	}
	
	@Test
	public void testMkdirShort() throws InvalidPathException, IOException{
		String path = "new";
		assertEquals(0, DirectoryOpt.mkdir(path));
		Path p =  absPath(Paths.get(path));
		assertTrue(Files.exists(p));
		assertTrue(Files.isDirectory(p));
		Files.delete(p);
	}
	
	@Test
	private void textMkdirEx(){
		String path = "???***";
		try {
			DirectoryOpt.mkdir(path);
		} catch (InvalidPathException e) {
			assertTrue(true);
		} catch (IOException e) {
		}
		fail();
	}
	
	@Test 
	public void testListFiles() throws InvalidPathException, IOException{
		String path = System.getProperty("user.dir");
		DirectoryOpt.mkdir("Папка");
		Path newFile = absPath(Paths.get(path + "\\Папка\\Файл"));
		newFile = Files.createFile(newFile);
		Path newDir = newFile.getParent();
		List<Path> list;
		try {
			list = DirectoryOpt.listFiles(newDir.toString());
			boolean flag = false;
			for (Path f :list){
				if (f.equals(newFile)){
					flag = true;
				}
			}
			assertTrue(flag);
			Files.delete(newFile);
			Files.delete(newDir);
		} catch (InvalidPathException | IOException e) {
			fail();
		}
	}
	
	@Test
	public void testListFilesVoid() throws IOException{
		Path path = Paths.get(System.getProperty("user.dir") + "\\Файл");
		path = Files.createFile(path);
		boolean flag = false;
		List<Path> list = DirectoryOpt.listFiles();
		for (Path f :list){
			if (f.equals(path)){
				flag = true;
			}
		}
		Files.delete(path);
		assertTrue(flag);
	}
	
	@Test 
	public void testChangeDir(){
		String path = "C:\\";
		DirectoryOpt.changeDir(path);
		assertEquals(path.toString(), System.getProperty("user.dir"));
	}
	
	@Test
	public void testChangeDirNoFile(){
		String path = "Это не те дроиды, которых вы ищете";
		assertFalse(DirectoryOpt.changeDir(path));
	}
	
	@Test
	public void testChangeDirYesFile() throws InvalidPathException, IOException{
		String path = "Это не те дроиды, которых вы ищете";
		DirectoryOpt.mkdir(path);
		assertTrue(DirectoryOpt.changeDir(path));
		Path p = Paths.get(path);
		if (Files.exists(p)){
			try {
				Files.delete(p);
			} catch (IOException e) {
			}
		}
	}
	
	@Test 
	public void testListFilesNE() throws IOException{
		Path path = Paths.get("Это не те дроиды, которых вы ищете");
		List<Path> list = DirectoryOpt.listFiles(App.absPath(path).toString());
		assertNull(list);
	}
	
	@Test
	public void testGoUp(){
		Path path = Paths.get(System.getProperty("user.dir"));
		DirectoryOpt.goUp();
		assertEquals(path.getParent().toString(), System.getProperty("user.dir"));
	}
	
	@Test
	public void testGoUpRoot(){
		String path = "C:\\";
		DirectoryOpt.changeDir(path);
		assertFalse(DirectoryOpt.goUp());
	}



}
