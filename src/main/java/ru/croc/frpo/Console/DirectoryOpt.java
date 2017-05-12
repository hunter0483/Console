package ru.croc.frpo.Console;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Данный класс содержит статические методы для работы с директориями в консоли.
 * @author Елизавета Охота
 *
 */
public class DirectoryOpt {
	
	/**
	 * Метод создаёт директорию с указанным именем в текущей директории.
	 * @param name - Строка с названием новой папки для сооздания.
	 * @return 0 - если папка успешно создана.
	 * 1 - если указано не имя папки, а путь, содержащий несколько директорий
	 * 2 - если папку невозможно создать, потому что она уже существет
	 * @throws InvalidPathException
	 * @throws IOException 
	 */
	public static int mkdir(String name) throws InvalidPathException, IOException {
		/*
		 * Если название папки состоит из иерархии папок
		 */
		if (name.contains("\\")) {
			return 1;
		}
		Path path = Paths.get(name); //throws InvalidPathException
		Path folder = App.absPath(path);
		/*
		 * Если дирекотрия не существует, создаём ее.
		 */
		if(!Files.exists(folder)){
			folder = Files.createDirectory(folder); //throws IOException
			return 0;
		} else{
			return 2;
		}
	}

	/**
	 * Этот метод возвращает список файлов в директории по указанному пути.
	 * @param path - Путь к директории, из которой читаем файлы. Может быть как абсолютный, так и относительный.
	 * @return Возвращает список файлов, находящихся в данной директории. Если директория пустая - пустой список.
	 * Если директории не существует - null.
	 * @throws IOException Во время итерации по файлам директории могут возникнуть ошибки доступа.
	 * @throws InvalidPathException При превращении строкив путь.
	 */
	 static List<Path> listFiles(String pathS) throws IOException, InvalidPathException {
		Path folder = App.absPath(Paths.get(pathS));
		/*
		 * Если указанной директории не существует, возвращаем null
		 */
		if (!Files.exists(folder)){
			return null;
		}
	    List<Path> list = new ArrayList<>();
	    try (DirectoryStream<Path> stream = Files.newDirectoryStream(folder)) {
		    /*
		     * Итерация по всем файлам в указанной директории и добавление путей в список
		     */
	        for (Path entry: stream) {
	            list.add(entry);
	        }
	    } catch (DirectoryIteratorException ex) {
	        throw ex.getCause();
	    }
	    return list;
	}
	
	/**
	 * Этот метод возвращает список файлов в корневой директории.
	 * @return Возвращает список файлов, находящийся в директории.
	 * @throws IOException 
	 */
	static List<Path> listFiles() throws IOException {
		String path = Paths.get(System.getProperty("user.dir")).toString();
		return listFiles(path);
	}

	/**
	 * Этот метод печатает список файлов, указывая, директория это, или файл.
	 * @param list - Массив файлов для печати. Сообщит, если файлы отсутствуют или если директория не существует.
	 */
	 static void printFiles(List<Path> list){
		if (list != null){
			if (list.isEmpty()){
				System.out.println("В этой директории отсутствуют файлы.");
			}
			for (Path path: list){
				if(Files.isDirectory(path)){
					System.out.print("Директория: ");
				} else{
					System.out.print("Файл:       ");
				}
				System.out.println(path.getFileName());
			}
		} else{
			/*
			 * Метод listFiles может возвращать значение null, если директории не существует.
			 * Учитываем это.
			 */
			System.out.println("К сожалению, такой директории не существует.");
		}
	}


	
	/**
	 * Распечатывает файлы, располагающиеся в данной директории.
	 * @param path - Директория, из которой надо печатать файлы.
	 * @throws IOException 
	 */
	public static  void showFiles(String path) throws IOException{
		printFiles(listFiles(path));
	}
	
	/**
	 * Распечатывает файлы текущей директории.
	 * @throws IOException 
	 */
	public static  void showFiles() throws IOException{
		printFiles(listFiles());
	}

	/**
	 * Изменяет текущую директорию на заданную.
	 * @param path - Новая текущая директория. Как абсолютный, так и относительный путь.
	 * @return Если директория существует, возвращает true. Иначе false.
	 * @throws InvalidPathException при превращении строки в путь
	 */
	public static boolean changeDir(String path)  throws InvalidPathException {
		Path dir = App.absPath(Paths.get(path));
		if (Files.exists(dir)){
			System.setProperty("user.dir", dir.toString());
			return true;
		}
		else {
			return false;
		}
	}

	/**
	 * Перемещает текущую директорию на один шаг вверх (к родительской директории).
	 * @return Возващает true, если текущая директория успешно перемещена.
	 * Возвращает false, если директории выше не существует.
	 */
	public static boolean goUp() {
		Path directory = Paths.get(System.getProperty("user.dir"));
		Path parent = directory.getParent();
		if (parent == null) {
			return false;
		} else {
			System.setProperty("user.dir", parent.toString());
			return true;
		}
	}
	
}
