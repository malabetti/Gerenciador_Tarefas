package com.mycompany.prova_betti;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

public class Prova_betti {

    public static File file = new File("tarefas.txt");
    public static Scanner scanner = new Scanner(System.in);
    
    public static ArrayList tarefas = new ArrayList();
    public static int max_id = 0;
    
    public static void main(String[] args) {
        
        if(file.exists()){
            ler_file();
        }
        menu();
    }
    
    static void ler_file(){
        try{
            FileReader reader = new FileReader(file);
            BufferedReader bufferedreader = new BufferedReader(reader);
            String linha;
            while((linha = bufferedreader.readLine()) != null){
                tarefas.add(linha);
                max_id++;
            }
            bufferedreader.close();
        }
        catch(IOException e){
                System.out.println("Erro: " + e);
        }
    }
    
    static void adicionar(){
        System.out.println("\nCadastro da tarefa:");
        System.out.print("Descrição da tarefa: ");
        String descricao = scanner.nextLine();
        System.out.print("Categoria da tarefa: ");
        String categoria = scanner.nextLine();
        
        try{
            FileWriter writer = new FileWriter(file, true);
            String nova_tarefa = max_id + ";" + descricao + ";" + categoria + "\n";
            writer.write(nova_tarefa);
            tarefas.add(nova_tarefa);
            max_id++;
            writer.close();
        }
        catch(IOException e){
            System.out.println("Erro: " + e);
        }
    }
    
    static void exibir(){
        Iterator it = tarefas.iterator();
        if(file.exists()){
            System.out.print("""
                             Deseja exibir:
                             1. Todas
                             2. Filtrar categoria
                             -> """);
            int opc = Integer.parseInt(scanner.nextLine());
            switch(opc){
                case 1 -> {
                    while(it.hasNext()){
                        String s = (String) it.next();
                        String tarefa[] = s.split(";");
                        System.out.println("\n------------");
                        System.out.println("ID: " + tarefa[0]);
                        System.out.println("Descrição: " + tarefa[1]);
                        System.out.println("Categoria: " + tarefa[2]);
                        System.out.println("------------");
                    }
                }
                case 2 -> {
                    System.out.print("Categoria: ");
                    String categoria = scanner.nextLine();
                    while(it.hasNext()){
                        String s = (String) it.next();
                        String tarefa[] = s.split(";");
                        if(tarefa[2].equals(categoria) || tarefa[2].equals(categoria.concat("\n"))){
                            System.out.println("\n------------");
                            System.out.println("ID: " + tarefa[0]);
                            System.out.println("Descrição: " + tarefa[1]);
                            System.out.println("Categoria: " + tarefa[2]);
                            System.out.println("------------");
                        }
                    }
                }
                default -> {
                    System.out.println("\nOpção inválida!");
                }
            }
        }
        else{
            System.out.println("\nNão há tarefas cadastradas!");
        }
    }
    
    static void atualizar(){
        if(file.exists()){
            System.out.print("Digite o ID da tarefa que deseja atualizar: ");
            int id = Integer.parseInt(scanner.nextLine());
            if(id < max_id && id >= 0){
                String atualizacao = (String) tarefas.get(id);
                String tarefa[] = atualizacao.split(";");
                System.out.print("""
                                 Deseja Atualizar:
                                 1. Descrição
                                 2. Categoria
                                 -> """);
                int opc = Integer.parseInt(scanner.nextLine());
                switch(opc){
                    case 1 -> {
                        System.out.print("Digite a nova descrição: ");
                        String desc = scanner.nextLine();
                        tarefa[1] = desc;
                        tarefas.set(id, tarefa[0] + ";" + tarefa[1] + ";" + tarefa[2]);
                        reescreve_file();
                    }
                    case 2 -> {
                        System.out.print("Digite a nova categoria: ");
                        String categoria = scanner.nextLine();
                        tarefa[2] = categoria;
                        tarefas.set(id, tarefa[0] + ";" + tarefa[1] + ";" + tarefa[2]);
                        reescreve_file();
                    }
                    default -> {
                        System.out.println("\nOpção inválida!");
                    }
                }
            }
            else{
                System.out.println("\nID inválido!");
            }
        }
        else{
            System.out.println("\nNão há tarefas cadastradas!");
        }
    }
    
    static void remover(){
        if(file.exists()){
            if(tarefas.size() == 1){
                System.out.println("Deve haver pelo menos uma tarefa cadastrada!");
                return;
            }
            System.out.print("Digite o ID da tarefa que deseja remover: ");
            int id = Integer.parseInt(scanner.nextLine());
            if(id < max_id && id >= 0){
                Iterator it = tarefas.iterator();
                while(it.hasNext()){
                    String linha = (String) it.next();
                    String tarefa[] = linha.split(";");
                    if(Integer.parseInt(tarefa[0]) > id){
                        int val = Integer.parseInt(tarefa[0]) - 1;
                        tarefas.set(Integer.parseInt(tarefa[0]), val + ";" + tarefa[1] + ";" + tarefa[2]);
                    }
                }
                tarefas.remove(id);
                reescreve_file();
                System.out.println("Tarefa removida!");
                max_id--;
            }
            else{
                System.out.println("\nID inválido!");
            }
        }
        else{
            System.out.println("\nNão há tarefas cadastradas!");
        }
    }
    
    static void gerenciar_categorias(){
        if(!file.exists()){
            System.out.println("Não há tarefas cadastradas!");
            System.out.println("*para adicionar categoria, é necessário criar uma tarefa com ela*");
            return;
        }
        while(true){
            System.out.print("""
                            \nDeseja:
                            1. Exibir categorias
                            2. Atualizar categoria
                            3. Excluir categoria
                            4. Sair
                            *para adicionar categoria, é necessário criar uma tarefa com ela*
                            -> """);
            int opc = Integer.parseInt(scanner.nextLine());
            switch(opc){
                case 1 -> {
                   Iterator it = tarefas.iterator();
                   Set categorias = new HashSet(); //usei set achando que não adicionava se já tivesse o elemento, mas adiciona sim
                   while(it.hasNext()){
                       String s = (String) it.next();
                       String tarefa[] = s.split(";");
                       if(!categorias.contains(tarefa[2])){ //então, coloquei esse if
                           categorias.add(tarefa[2]);
                       }
                   }
                   it = categorias.iterator();
                   int i = 1;
                   System.out.println("\nCategorias:");
                   while(it.hasNext()){
                       System.out.println(i + ". " + it.next());
                       i++;
                   }
                }
                case 2 -> {
                    System.out.print("Categoria que deseja atualizar: ");
                    String antiga = scanner.nextLine();
                    System.out.print("Deseja atualizar para: ");
                    String nova = scanner.nextLine();
                    Iterator it = tarefas.iterator();
                    while(it.hasNext()){
                       String s = (String) it.next();
                       String tarefa[] = s.split(";");
                       if(tarefa[2].equals(antiga) || tarefa[2].equals(antiga.concat("\n"))){
                           tarefa[2] = nova;
                       }
                       tarefas.set(Integer.parseInt(tarefa[0]), tarefa[0] + ";" + tarefa[1] + ";" + tarefa[2]);
                    }
                    reescreve_file();
                }
                case 3 -> {
                    System.out.print("Categoria que deseja remover: ");
                    String categoria = scanner.nextLine();
                    
                    Iterator it = tarefas.iterator();
                    int prox = -1;
                    Set categorias = new HashSet(); //usei set achando que não adicionava se já tivesse o elemento, mas adiciona sim
                    while(it.hasNext()){
                        String s = (String) it.next();
                        String tarefa[] = s.split(";");
                        if(!categorias.contains(tarefa[2])){ //então, coloquei esse if
                            categorias.add(tarefa[2]);
                        }
                        if(tarefa[2].equals(categoria) || tarefa[2].equals(categoria.concat("\n"))){
                            prox = Integer.parseInt(tarefa[0]);
                        }
                    }
                    while(categorias.contains(categoria)){
                        if(tarefas.size() == 1){
                            System.out.println("Deve haver pelo menos uma tarefa cadastrada!");
                            System.out.println("Foi mantido uma tarefa da categoria: " + categoria + " por conta disso!");
                            break;
                        }
                        
                        Iterator ita = tarefas.iterator();
                        while(ita.hasNext()){
                            String linha = (String) ita.next();
                            String tarefa[] = linha.split(";");
                            if(Integer.parseInt(tarefa[0]) > prox){
                                int val = Integer.parseInt(tarefa[0]) - 1;
                                tarefas.set(Integer.parseInt(tarefa[0]), val + ";" + tarefa[1] + ";" + tarefa[2]);
                            }
                        }
                        tarefas.remove(prox);
                        reescreve_file();
                        max_id--;
                        
                        Iterator a = tarefas.iterator();
                        categorias = new HashSet();
                        while(a.hasNext()){
                            String s = (String) a.next();
                            String tarefa[] = s.split(";");
                            if(!categorias.contains(tarefa[2])){
                                categorias.add(tarefa[2]);
                            }
                            if(tarefa[2].equals(categoria) || tarefa[2].equals(categoria.concat("\n"))){
                                prox = Integer.parseInt(tarefa[0]);
                            }
                        }
                    }
                    
                    reescreve_file();
                }
                case 4 -> {
                    System.out.println("\nRetornando!");
                    return;
                }
                default -> {
                    System.out.println("\nOpção inválida!");
                }
            }
        }
    }
    
    static void reescreve_file(){
        Iterator it = tarefas.iterator();
        try{
            FileWriter writer = new FileWriter(file);
            while(it.hasNext()){
                writer.write(it.next() + "\n");
            }
            writer.close();
        }
        catch(IOException e){
            System.out.println("Erro: " + e);
        }
    }
    
    static void menu(){
        while(true){
            System.out.print("""
                             \nMenu:
                             1. Adicionar tarefas
                             2. Exibir tarefas
                             3. Atualizar tarefas
                             4. Excluir tarefas
                             5. Gerenciar categorias ou tags
                             6. Sair
                             -> """);
            int opc = Integer.parseInt(scanner.nextLine());
            switch(opc){
                case 1 -> {
                    adicionar();
                }
                case 2 -> {
                    exibir();
                }
                case 3 -> {
                    atualizar();
                }
                case 4 -> {
                    remover();
                }
                case 5 -> {
                    gerenciar_categorias();
                }
                case 6 -> {
                    System.out.println("\nSaindo!");
                    return;
                }
                default -> {
                    System.out.println("\nOpção inválida!");
                }
            }
        }
    }
}
