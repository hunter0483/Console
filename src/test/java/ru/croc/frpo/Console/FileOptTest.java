package ru.croc.frpo.Console;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import junit.framework.TestCase;

public class FileOptTest extends TestCase {
	

	@Test
	public void testNewFileSucess() throws IOException{
		String name = "Файлик";
		assertEquals(0, FileOpt.newFile(name));
		Path p =  App.absPath(Paths.get(name));
		assertTrue(Files.exists(p));
		assertTrue(Files.isRegularFile(p));
		Files.delete(p);
	}
	
	
	@Test
	public void testNewFileExists() throws InvalidPathException, IOException{
		String name = "file111";
		FileOpt.newFile(name);
		assertEquals(2, FileOpt.newFile(name));
		Files.delete(App.absPath(Paths.get(name)));
	}
	
	@Test 
	public void testFileLong() throws InvalidPathException, IOException{
		String name = "new\\sdf";
		assertEquals(1, FileOpt.newFile(name));
	}
	
	@Test
	public void testAddToFile() throws IOException{
		String pathS = "text.txt";
		FileOpt.newFile(pathS);
		Path path = App.absPath(Paths.get(pathS));
		List<String> lines = new ArrayList<String>();
		String firstLine = "Здравствуйте!";
		String secondLine = "Сегодня с вами я, ваш любимый ведущий!";
		lines.add(firstLine);
		lines.add(secondLine);
		FileOpt.appendFile(pathS, lines);
		List<String> result = Files.readAllLines(path);
		assertEquals(firstLine, result.get(0));
		assertEquals(secondLine, result.get(1));
		Files.delete(path);
	}
	
	@Test
	public void testAddToFileNotExists() throws IOException{
		String pathS = "text.txt";
		Path path = App.absPath(Paths.get(pathS));
		List<String> lines = new ArrayList<String>();
		String firstLine = "Здравствуйте!";
		String secondLine = "Сегодня с вами я, ваш любимый ведущий!";
		lines.add(firstLine);
		lines.add(secondLine);
		FileOpt.appendFile(pathS, lines);
		List<String> result = Files.readAllLines(path);
		assertEquals(firstLine, result.get(0));
		assertEquals(secondLine, result.get(1));
		Files.delete(path);
	}
	
	@Test
	public void testReadFile() throws InvalidPathException, IOException{
		String pathS = "text.txt";
		Path path = App.absPath(Paths.get(pathS));
		List<String> lines = new ArrayList<String>();
		String firstLine = "Здравствуйте!";
		String secondLine = "Сегодня с вами я, ваш любимый ведущий!";
		lines.add(firstLine);
		lines.add(secondLine);
		FileOpt.appendFile(pathS, lines);
		int n = 2;
		List<String> result = FileOpt.readFileLines(pathS, n);
		assertEquals(n, result.size());
		assertEquals(firstLine, result.get(0));
		assertEquals(secondLine, result.get(1));
		Files.delete(path);
	}
	
	@Test
	public void testReadLongFile() throws InvalidPathException, IOException{
		String pathS = "text.txt";
		Path path = App.absPath(Paths.get(pathS));
		List<String> lines = new ArrayList<String>();
		lines.add("Здравствуйте!");
		lines.add("Сегодня с вами я, ваш любимый ведущий!");
		lines.add("Приём!");
		lines.add("Жду вас у телеэкранов завтра в 22-00.");
		FileOpt.appendFile(pathS, lines);
		int n = 2;
		List<String> result = FileOpt.readFileLines(pathS, n);
		assertEquals(n, result.size());
		Files.delete(path);
	}
	
	@Test
	public void testReadEmptyFile() throws InvalidPathException, IOException{
		String pathS = "Test.txt";
		Path path = App.absPath(Paths.get(pathS));
		path = Files.createFile(path);
		List<String> lines = FileOpt.readFileLines(pathS, 10);
		assertEquals(0,lines.size());
		Files.delete(path);
	}
	
	@Test
	public void testReadNofile() throws InvalidPathException, IOException{
		String pathS = "Test.txt";
		List<String> lines = FileOpt.readFileLines(pathS, 10);
		assertNull(lines);
	}
	
	

}
