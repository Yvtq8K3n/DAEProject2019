/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejbs;

import converter.LocalDateAdapter;
import dtos.AdministratorDTO;
import dtos.ClientDTO;
import dtos.CommentDTO;
import dtos.ConfigurationDTO;
import dtos.ModuleDTO;
import dtos.ParameterDTO;
import dtos.TemplateDTO;
import java.time.LocalDate;
import javax.annotation.PostConstruct;
import javax.annotation.security.RunAs;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
/**
 *
 * @author Joao Marquez
 */
@RunAs("Admin")
@Singleton
@Startup 
public class ConfigBean {

    @EJB//Dà uma instancida do EJB ClientBean
    private ClientBean clientBean;
    
    @EJB//Dà uma istancida do EJB AdminstratorBean
    private AdministratorBean adminstratorBean;
    
    @EJB//Dà uma instancida do EJB TemplateBean
    private TemplateBean templateBean;
    
    @EJB//Dà uma instancida do EJB ConfigurationBean
    private ConfigurationBean configurationBean;
    
    @EJB
    private CommentBean commentBean;
      
    @PostConstruct
    public void populateDB() {
        try{
            //Create Users
            clientBean.create(new ClientDTO("client1", "secret", "Manuel", "oleksandrsopilkov@gmail.com", "Av. José Maceda", "918 923 232"));
            clientBean.create(new ClientDTO("client2", "secret", "Teresa", "dae.ei.ipleiria@gmail.com", "Av. Alberto Alves", "+00351 256 0033 12"));
            adminstratorBean.create(new AdministratorDTO("administrator1", "secret", "Manuel", "dae.ei.ipleiria.2019@gmail.com", "Director"));
            adminstratorBean.create(new AdministratorDTO("administrator2", "secret", "João Marques", "dae.ei.ipleiria@gmail.com", "Vendedor"));
            
            //Create Configurations
            configurationBean.create(new ConfigurationDTO(null,"Office 2017","Service for life", "V1.5.0", "client1",ConfigurationDTO.Status.SUSPEND,LocalDateAdapter.marshal(LocalDate.of(2019,11,10))));
            configurationBean.create(new ConfigurationDTO(null,"Karsperkey","Securing your information", "V1.7.8", "client1",ConfigurationDTO.Status.ACTIVE,LocalDateAdapter.marshal(LocalDate.of(2019,12,23))));
            configurationBean.create(new ConfigurationDTO(null,"CMTV","The wost tv news ever created!", "V1.5.0", "client1",ConfigurationDTO.Status.ACTIVE,LocalDateAdapter.marshal(LocalDate.of(2020,12,31))));
            configurationBean.create(new ConfigurationDTO(null,"Pokemon","Hey! Someone got catche them all, lets hope is not you", "V1.7.8", "client1",ConfigurationDTO.Status.ACTIVE,LocalDateAdapter.marshal(LocalDate.of(2015,6,21))));
            
            //Create Templates
            templateBean.create(new TemplateDTO(1L, "Office 2017 Professional Edition", "A combination of escencial tools that provide, the best of personal computers"));
            templateBean.create(new TemplateDTO(2L, "Wall of China", "This template contain all the procedures in order to sucessfully deploy a wall in a huge scale"));
            templateBean.create(new TemplateDTO(3L, "Under the Sea!", "A jorney under the world wide sea! Kind of big, thought may be a long long jorney."));
            templateBean.create(new TemplateDTO(4L, "Cry your Heart Out!", "Life isnt always great, this pack will make u feel better, at least thats what we hope."));
        }catch (Exception ex){
            System.out.println(ex);
        }

        try{
            //Create module for Configuration:Office 2017
            configurationBean.createModule(1L,new ModuleDTO(null,"Word 2017", "21.40"));
            configurationBean.createModule(1L, new ModuleDTO(null,"PowerPoint 2017", "32.1"));
            configurationBean.createModule(1L, new ModuleDTO(null,"Excel 2017", "11.30"));
            
            //Create module for Configuration:Pokemon
            configurationBean.createModule(4L, new ModuleDTO(null,"Charizard", "21.40"));
            configurationBean.createModule(4L, new ModuleDTO(null,"Pikachu", "32.1"));
            configurationBean.createModule(4L, new ModuleDTO(null,"Onix", "21.40"));
            configurationBean.createModule(4L, new ModuleDTO(null,"Salamance", "32.1"));
            
            //Create module for Template:Office 2017
            templateBean.createModule(5L, new ModuleDTO(null,"Word 2017", "21.40"));
            templateBean.createModule(5L, new ModuleDTO(null,"PowerPoint 2017", "32.1"));
            templateBean.createModule(5L, new ModuleDTO(null,"Excel 2017", "11.30"));
            templateBean.createModule(5L, new ModuleDTO(null,"OneNote 2017", "32.22"));
            templateBean.createModule(5L, new ModuleDTO(null,"Publisher 2017", "45.34"));
        }catch (Exception ex){
            System.out.println(ex);
        }
        
        try{
            //Create parameter for Configuration:Office 2017
            configurationBean.createParameter(1L,new ParameterDTO(null,ParameterDTO.MaterialType.HARDWARE, "Placa Gráfica", "Para conseguir um maior desempenho gráfico em jogos", LocalDateAdapter.marshal(LocalDate.of(2021,9,12))));
            configurationBean.createParameter(1L,new ParameterDTO(null,ParameterDTO.MaterialType.CLOUD, "Meo Cloud", "My Cloud Home is the perfect storage solution to easily keep all your photos", LocalDateAdapter.marshal(LocalDate.of(2019,11,11))));
            configurationBean.createParameter(1L,new ParameterDTO(null,ParameterDTO.MaterialType.EXTENSIONS, "Develop Extensions", "After reading the Getting Started tutorial and Overview", LocalDateAdapter.marshal(LocalDate.of(2021,1,10))));
            configurationBean.createParameter(1L,new ParameterDTO(null,ParameterDTO.MaterialType.REPOSITORY, "Component repository", "field of configuration management", LocalDateAdapter.marshal(LocalDate.of(2019,5,3))));
            
            //Create module for Configuration:Pokemon
            configurationBean.createParameter(2L,new ParameterDTO(null,ParameterDTO.MaterialType.CLOUD, "Cloud Storage", "Google Cloud Storage is unified object storage for developers and enterprises", LocalDateAdapter.marshal(LocalDate.of(2021,2,16))));
            //Create module for Template:Office 2017
            configurationBean.createParameter(5L,new ParameterDTO(null,ParameterDTO.MaterialType.HARDWARE, "MySensors", "The MySensors core team have created a few hardware components to ease up life", LocalDateAdapter.marshal(LocalDate.of(2019,7,5))));
        }catch (Exception ex){
            System.out.println(ex);
        }
        
        try{
            //Long dad = 21L;
            Long dad = 26L;
            commentBean.create(new CommentDTO(null, null, null, 1L, "Im Your Granpha!","client1"));//granpha
            commentBean.create(new CommentDTO(null, dad, null, 1L, "Im Your True DAD!", "administrator1"));//dad
            commentBean.create(new CommentDTO(null, dad+1, null, 1L, "Im Child1!","client1"));//child1
            commentBean.create(new CommentDTO(null, dad+1, null, 1L, "Im Child2!","client1"));//child2
            commentBean.create(new CommentDTO(null, dad, null, 1L, "I Ain't Your DAD!", "client1"));//dad
        }catch (Exception ex){
            System.out.println(ex);
        }
        
        //productBean.create(new ConfigurationDTO(1L, "PRODUCT1", "Im just a normal product", "v1.10", "client1"));
        //productBean.create(new ConfigurationDTO(2L, "REMIDA", "Website about energy", "v1.40", "client1"));
        //productBean.create(new ConfigurationDTO(3L, "JAVA EE Aplication", "Complex project", "v1.40", "client1"));
        //clientBean.addProduct("client1", new Long(1));

        /*Comment parent = commentBean.create(null, 1,"Initial comment");
        Comment child = commentBean.create(parent.getId(), 1,"Not initial comment");
        parent.addChildren(child);

        moduleBean.create("module 1", "1.0");

        configurationBean.addModule(new Long(2), new Long(5));
        System.out.println("I FINISH");*/
    }
}
