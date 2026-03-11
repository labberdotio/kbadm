//package io.labber.kbadm;
//
//import java.io.File;
//import java.util.List;
//
//// import com.ankit.rag_demo.model.Answer;
//// import com.ankit.rag_demo.model.Question;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.ai.chat.client.ChatClient;
//import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
//import org.springframework.ai.chat.model.ChatResponse;
//import org.springframework.ai.document.Document;
//import org.springframework.ai.reader.tika.TikaDocumentReader;
//import org.springframework.ai.transformer.splitter.TokenTextSplitter;
//import org.springframework.ai.vectorstore.SearchRequest;
//// import org.springframework.ai.vectorstore.SearchRequest;
//import org.springframework.ai.vectorstore.VectorStore;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.core.io.FileSystemResource;
//import org.springframework.core.io.Resource;
//
//@Service
//public class RagService {
//
//    private static final Logger logger = LoggerFactory.getLogger(RagService.class);
//
//    // private final ChatClient chatClient;
//    // private final VectorStore vectorStore;
//
//    @Autowired
//    protected ChatClient customOllamaChatClient;
//
//    @Autowired
//    protected VectorStore vectorStore;
//
//    // public RagService(ChatClient.Builder chatClientBuilder, VectorStore vectorStore) {
//    //     this.chatClient = chatClientBuilder.build();
//    //     this.vectorStore = vectorStore;
//    // }
//
//    @Value("${app.resource}")
//    private Resource documentResource;
//    
//    public Answer askQuestion(Question question){
//        logger.info("Processing question: {}", question.question());
//
//        try{
//            return askWithAdvisor(question);
//        }
//        catch(Exception e){
//            logger.error("Error processing question", e);
//            return new Answer("Sorry, I encountered an error while processing your question.");
//        }
//    }
//
//    private Answer askWithAdvisor(Question question){
//    	
////    	TikaDocumentReader documentReader = new TikaDocumentReader(documentResource);
////    	List<Document> documents = documentReader.get();
////    	for( Document document : documents ) {
////    		System.out.println(" >> DOC: " + document.getId());
////    	}
////    	TokenTextSplitter textSplitter = new TokenTextSplitter(500, 100,5,1000,true);
////    	List<Document> splitDocuments = textSplitter.apply(documents);
////    	for( Document document : splitDocuments ) {
////    		System.out.println(" >> SDOC: " + document.getId());
////    	}
////    	vectorStore.add(splitDocuments);
////   
////    	// vectorStore.
////    	// SearchRequest.builder().query(question.question()).build();
////    	List<Document> relevantDocuments = vectorStore.similaritySearch(SearchRequest.builder().query(question.question()).build()); // .withTopK(5));
////    	for( Document document : relevantDocuments ) {
////    		System.out.println(" >> RDOC: " + document.getId());
////    	}
//
////    	String[] books = {
////"Computing/FreeBSD/FreeBSD 13.0 Handbook.pdf", 
////"Computing/FreeBSD/FreeBSD 11.4 Developers Handbook.pdf", 
////"Computing/FreeBSD/O'Reilly - The Complete FreeBSD.pdf", 
////"Computing/FreeBSD/FreeBSD 14.2 Handbook.pdf", 
////"Computing/FreeBSD/FreeBSD 10.3 FAQ.pdf", 
////"Computing/FreeBSD/FreeBSD 11.4 Architecture Handbook.pdf", 
////"Computing/FreeBSD/FreeBSD 14.0 Handbook.pdf", 
////"Computing/FreeBSD/FreeBSD 13.2 Handbook.pdf", 
////"Computing/FreeBSD/FreeBSD 14.0 Developers Handbook.pdf", 
////"Computing/FreeBSD/FreeBSD 13.2 Architecture Handbook.pdf", 
//////"Computing/FreeBSD/FreeBSD 14.2 Developers Handbook.pdf", .1
////"Computing/FreeBSD/FreeBSD 13.2 Developers Handbook.pdf", 
////"Computing/FreeBSD/FreeBSD 14.2 FAQ.pdf", 
//////"Computing/FreeBSD/FreeBSD 14.2 Handbook.pdf", .1
////"Computing/FreeBSD/FreeBSD 11.4 Handbook.pdf", 
////"Computing/FreeBSD/O'Reilly - The Complete FreeBSD, 4th ed.pdf", 
////"Computing/FreeBSD/FreeBSD 14.0 Architecture Handbook.pdf", 
////"Computing/FreeBSD/FreeBSD 13.2 FAQ.pdf", 
////"Computing/FreeBSD/FreeBSD 10.3 Architecture Handbook.pdf", 
////"Computing/FreeBSD/FreeBSD 12.2 FAQ.pdf", 
////"Computing/FreeBSD/FreeBSD 14.0 FAQ.pdf", 
////"Computing/FreeBSD/FreeBSD 13.0 Developers Handbook.pdf", 
////"Computing/FreeBSD/The Design and Implementation of the 4.4BSD Operating System.pdf", 
////"Computing/FreeBSD/BSD UNIX Toolbox 1000+ Commands for FreeBSD, OpenBSD and NetBSD.pdf", 
////"Computing/FreeBSD/FreeBSD 10.3 Handbook.pdf", 
////"Computing/FreeBSD/FreeBSD 13.0 Architecture Handbook.pdf", 
////"Computing/FreeBSD/FreeBSD 11.4 FAQ.pdf", 
////"Computing/FreeBSD/The Complete FreeBSD, 4th Edition.pdf", 
////"Computing/FreeBSD/FreeBSD 13.0 FAQ.pdf", 
////"Computing/FreeBSD/FreeBSD 12.2 Developers Handbook.pdf", 
////"Computing/FreeBSD/The Complete FreeBSD 4ed.pdf", 
////"Computing/FreeBSD/FreeBSD 14.2 Developers Handbook.pdf", 
////"Computing/FreeBSD/FreeBSD 10.3 Developers Handbook.pdf", 
////"Computing/FreeBSD/FreeBSD 12.2 Architecture Handbook.pdf", 
////"Computing/FreeBSD/FreeBSD 12.2 Handbook.pdf", 
////"Computing/FreeBSD/FreeBSD 14.2 Architecture Handbook.pdf", 
////"Computing/FreeBSD/Absolute FreeBSD.pdf", 
////"Computing/UNIX/UNIX Programmers Manual - 2ed.pdf", 
////"Computing/UNIX/unixinanutshell_4thedition.pdf", 
////"Computing/UNIX/UNIX Programmers Manual - 4.2BSD 2C.pdf", 
////"Computing/UNIX/A Commentary on the Sixth Edition Unix Operating System.pdf", 
////"Computing/UNIX/UNIX Programmers Manual - 1ed.pdf", 
////"Computing/UNIX/The Design Of the Unix Operating System.pdf", 
////"Computing/UNIX/The Design of Unix Operating System.pdf", 
////"Computing/UNIX/UNIX Programmers Manual - 6ed.pdf", 
////"Computing/UNIX/Mastering Unix.pdf", 
////"Computing/UNIX/Unix Operating System Source Code Level 6.pdf", 
////"Computing/UNIX/The UNIX-HATERS Handbook.pdf", 
////"Computing/UNIX/Unix.in.a.Nutshell.pdf", 
////"Computing/UNIX/UNIX Programming Environment.pdf", 
////"Computing/UNIX/UNIX and Linux System Administration Handbook, 4ed.pdf", 
////"Computing/UNIX/UNIX Programmers Manual - 4ed.pdf", 
////"Computing/UNIX/Beginning Unix.pdf", 
////"Computing/UNIX/A COMMENTARY ON THE SIXTH EDITION UNIX OPERATING SYSTEM.pdf", 
////"Computing/UNIX/UNIX and Linux System Administration Handbook, 5ed.pdf", 
////"Computing/UNIX/UNIX Programmers Manual - 5ed.pdf", 
////"Computing/UNIX/PreliminaryUnixImplementationDocument_Jun72.pdf", 
////"Computing/An Introduction To Neural Networks.pdf", 
////"Computing/Regex/Beginning Regular Expressions [Watt 2005-02-04].pdf", 
////"Computing/Regex/Regular Expressions Cookbook_ Detailed Solutions in Eight Programming Languages (2nd ed.) [Goyvaerts & Levithan 2012-09-06].pdf", 
////"Computing/Regex/Regular Expression Pocket Reference_ Regular Expressions for Perl, Ruby, PHP, Python, C, Java, and .NET (2nd ed.) [Stubblebine 2007-07-28].pdf", 
////"Computing/Regex/Introducing Regular Expressions_ Unraveling Regular Expressions, Step-by-Step [Fitzgerald 2012-08-03].pdf", 
////"Computing/Regex/Mastering Regular Expressions_ Powerful Techniques for Perl and Other Tools (2nd ed.) [Friedl 2002-07-15].pdf", 
////"Computing/Debian/Debian Reference 2007.pdf", 
////"Computing/Debian/The Debian System Concepts And Techniques.pdf", 
////"Computing/Debian/Debian Reference 2021.pdf", 
////"Computing/Debian/The Debian GNU Linux FAQ.pdf", 
////"Computing/Debian/The Debian Administrators Handbook.pdf", 
////"Computing/Modern Operating System - Tanenbaum.pdf", 
////"Computing/Red Hat/Red Hat Linux 5.2 Installation Guide.pdf", 
////"Computing/Red Hat/Red Hat Enterprise Linux 5 Administration Unleashed.pdf", 
////"Computing/Red Hat/Red Hat Enterprise Linux 8 Security hardening.pdf", 
////"Computing/Red Hat/Red Hat Enterprise Linux 8 Managing and monitoring security updates.pdf", 
////"Computing/Red Hat/Beginning Red Hat Linux 9.pdf", 
////"Computing/Red Hat/Red Hat Universal Base Images.pdf", 
////"Computing/Red Hat/Red Hat Enterprise Linux 6 Administration.pdf", 
////"Computing/Red Hat/Red Hat Enterprise Linux 8 Securing networks.pdf", 
////"Computing/Red Hat/Red Hat Enterprise Linux 7 SELinux Users and Administrators Guide.pdf", 
////"Computing/Red Hat/Red Hat Virtualization 4.4 REST API Guide.pdf", 
////"Computing/Red Hat/Red Hat Enterprise Linux 7 System Administrators Guide.pdf", 
////"Computing/Red Hat/Red_Hat_Linux_Complete_Reference.pdf", 
////"Computing/Red Hat/Red Hat Enterprise Linux 7 Security Guide.pdf", 
////"Computing/Red Hat/Red Hat Enterprise Linux 8 Using SELinux.pdf", 
////"Computing/Red Hat/ma-linux-management-ebook-f18267-201912-en.pdf", 
////"Computing/Emacs/emacs-29.2.pdf", 
////"Computing/Emacs/Learning GNU Emacs.pdf", 
////"Computing/Emacs/emacs-28.2.pdf", 
////"Computing/Emacs/emacs-26.3.pdf", 
////"Computing/Emacs/emacs-24.4.pdf", 
////"Computing/Emacs/emacs.pdf", 
////"Computing/Emacs/emacs-30.1.pdf", 
////"Computing/Genera_Concepts.pdf", 
////"Computing/AWK/Sed & Awk.pdf", 
////"Computing/AWK/The_AWK_Programming_Language_-_Aho,_Weinberger,_Kernighan.pdf", 
////"Computing/ZFS/ZFS.pdf", 
////"Computing/ZFS/ZFS2.pdf", 
////"Computing/K8S/Kubernetes Native Microservices.pdf", 
////"Computing/K8S/Cluster API and Declarative Kubernetes Management.pdf", 
////"Computing/K8S/Knative Cookbook.pdf", 
////"Computing/K8S/Kubernetes Patterns.pdf", 
////"Computing/K8S/Production Kubernetes.pdf", 
////"Computing/FreeNAS/FreeNAS 11.2-U3 User Guide.pdf", 
////"Computing/FreeNAS/TrueNAS-11.3-U5-User-Guide_screen.pdf", 
////"Computing/FreeNAS/FreeNAS 11.3-U5 User Guide.pdf", 
////"Computing/FreeNAS/CORE12.0Docs.pdf", 
////"Computing/Linux/Linux - The Complete Reference.pdf", 
////"Computing/Linux/Linux - The Complete Reference, 6 Ed.pdf", 
////"Computing/Linux/Beginning Linux Programming, 4th Edition.pdf", 
////"Computing/Linux/Linux System Programming.pdf", 
////"Computing/Linux/Gentoo Linux amd64 Handbook: Installing Gentoo - Gentoo Wiki.pdf", 
////"Computing/Linux/Gentoo Linux amd64 Handbook Installing Gentoo - Gentoo Wiki.pdf", 
////"Computing/Linux/Linux Administration Made Easy.pdf", 
////"Computing/Linux/Linux Network Administrators Guide 2.pdf", 
////"Computing/Linux/Running_Linux_4th_Edition.pdf", 
////"Computing/Linux/The Linux Users Guide.pdf", 
////"Computing/Linux/Linux In A Nutshell 6th Edition.pdf", 
////"Computing/Linux/Linux From Scratch 6.1.1.pdf", 
////"Computing/Linux/Linux Bible 8th Edition.pdf", 
////"Computing/Linux/Linux Kernel Module Programming Guide 1.1.0 2.pdf", 
////"Computing/Linux/The Linux Programmers Guide.pdf", 
////"Computing/Linux/Linux Installation and Getting Started 3.2.pdf", 
////"Computing/Linux/Linux Kernel Module Programming Guide 1.1.0 1.pdf", 
////"Computing/Linux/Learning-Modern-Linux-A-Handbook-for-the-Cloud-Native-Practitioner-by-Michael-Hausenblas_bibis.ir.pdf", 
////"Computing/Linux/Running Linux - Matthias Kalle Dalheimer and Matt Welsh.pdf", 
////"Computing/Linux/Linux Complete Command Reference.pdf", 
////"Computing/Linux/The Linux System Administrators Guide 0.6.2.pdf", 
////"Computing/Linux/Linux Kernel 2.4 Internals.pdf", 
////"Computing/Linux/SELinux_Notebook.pdf", 
////"Computing/Linux/Kali Linux Revealed.pdf", 
////"Computing/Linux/Linux From Scratch.pdf", 
////"Computing/Linux/The Linux Network Administrators Guide 1.0.pdf", 
////"Computing/Linux/The Linux Programmers Guide__.pdf", 
////"Computing/Linux/Linux Bible 2010 Edition.pdf", 
////"Computing/Linux/Linux From Scratch 7.7.pdf", 
////"Computing/Linux/Securing-Optimizing-Linux-RH-Edition-v1.3.pdf", 
////"Computing/Linux/Smart Home Automation with Linux.pdf", 
////"Computing/Medley/Medley-Primer.pdf", 
////"Computing/Medley/IRM.pdf", 
////"Computing/Medley/SunUserGuide.pdf", 
////"Computing/Genera_User_s_Guide.pdf", 
////"Computing/Slackware/Slackware Desktop Guide.pdf", 
////"Computing/Slackware/Slackware Linux Essentials.pdf", 
////"Computing/Slackware/slackpkg.pdf", 
////"Computing/Slackware/Configure your new Slackware System.pdf", 
////"Computing/Slackware/Enabling Sudo on Slackware.pdf", 
////"Computing/What Is SRE.pdf", 
////"Computing/Modern Operating Systems 2nd Ed by Tanenbaum (with pdf index).pdf", 
////"Computing/OpenBSD/Absolute OpenBSD - Unix For The Practical Paranoid (2003).pdf", 
////"Computing/OpenBSD/Absolute OpenBSD.pdf", 
////"Computing/Ubuntu/Ubuntu Server Guide 16.04.pdf", 
////"Computing/Ubuntu/Mastering Ubuntu Server.pdf", 
////"Computing/Ubuntu/Installing Ubuntu Server.pdf", 
////"Computing/Ubuntu/Ubuntu Unleashed 2019 Edition.pdf", 
////"Computing/Ubuntu/Ubuntu Server Guide 18.04.pdf", 
////"Computing/Ubuntu/Ubuntu Server Guide 20.04.pdf", 
////"Computing/Solaris/E39134.pdf", 
////"Computing/Solaris/E36855.pdf", 
////"Computing/Solaris/E36829.pdf", 
////"Computing/Solaris/E37516.pdf", 
////"Computing/Solaris/E36852.pdf", 
////"Computing/Solaris/E36820.pdf", 
////"Computing/Solaris/E36827.pdf", 
////"Computing/Solaris/E36812.pdf", 
////"Computing/Solaris/E36815.pdf", 
////"Computing/Solaris/E36869.pdf", 
////"Computing/Solaris/E37475.pdf", 
////"Computing/Solaris/E36867.pdf", 
////"Computing/Solaris/E36860.pdf", 
////"Computing/Solaris/E49624.pdf", 
////"Computing/Solaris/E37121.pdf", 
////"Computing/Solaris/E36804.pdf", 
////"Computing/Solaris/E37126.pdf", 
////"Computing/Solaris/E36803.pdf", 
////"Computing/Solaris/E38524.pdf", 
////"Computing/Solaris/E36836.pdf", 
////"Computing/Solaris/E36831.pdf", 
////"Computing/Solaris/E36843.pdf", 
////"Computing/Solaris/E36844.pdf", 
////"Computing/Solaris/E36838.pdf", 
////"Computing/Solaris/E36861.pdf", 
////"Computing/Solaris/E36866.pdf", 
////"Computing/Solaris/E36868.pdf", 
////"Computing/Solaris/E37474.pdf", 
////"Computing/Solaris/E54155.pdf", 
////"Computing/Solaris/E37473.pdf", 
////"Computing/Solaris/E36813.pdf", 
////"Computing/Solaris/E36826.pdf", 
////"Computing/Solaris/E36821.pdf", 
////"Computing/Solaris/E37517.pdf", 
////"Computing/Solaris/E36853.pdf", 
////"Computing/Solaris/E36828.pdf", 
////"Computing/Solaris/E36845.pdf", 
////"Computing/Solaris/E36842.pdf", 
////"Computing/Solaris/E36830.pdf", 
////"Computing/Solaris/E38254.pdf", 
////"Computing/Solaris/E36837.pdf", 
////"Computing/Solaris/E52463.pdf", 
////"Computing/Solaris/E37127.pdf", 
////"Computing/Solaris/E36802.pdf", 
////"Computing/Solaris/E36805.pdf", 
////"Computing/Solaris/E36841.pdf", 
////"Computing/Solaris/E36846.pdf", 
////"Computing/Solaris/E36848.pdf", 
////"Computing/Solaris/E36834.pdf", 
////"Computing/Solaris/E37123.pdf", 
////"Computing/Solaris/E36806.pdf", 
////"Computing/Solaris/E37629.pdf", 
////"Computing/Solaris/E54953.pdf", 
////"Computing/Solaris/E37124.pdf", 
////"Computing/Solaris/E36801.pdf", 
////"Computing/Solaris/E36819.pdf", 
////"Computing/Solaris/E36865.pdf", 
////"Computing/Solaris/E37631.pdf", 
////"Computing/Solaris/E36862.pdf", 
////"Computing/Solaris/E39499.pdf", 
////"Computing/Solaris/E36817.pdf", 
////"Computing/Solaris/E36822.pdf", 
////"Computing/Solaris/E39067.pdf", 
////"Computing/Solaris/E36859.pdf", 
////"Computing/Solaris/E36825.pdf", 
////"Computing/Solaris/E36857.pdf", 
////"Computing/Solaris/E36850.pdf", 
////"Computing/Solaris/E37125.pdf", 
////"Computing/Solaris/E36800.pdf", 
////"Computing/Solaris/E37122.pdf", 
////"Computing/Solaris/E36807.pdf", 
////"Computing/Solaris/E37628.pdf", 
////"Computing/Solaris/E36832.pdf", 
////"Computing/Solaris/E36849.pdf", 
////"Computing/Solaris/E36835.pdf", 
////"Computing/Solaris/E36847.pdf", 
////"Computing/Solaris/E36840.pdf", 
////"Computing/Solaris/E36799.pdf", 
////"Computing/Solaris/E36851.pdf", 
////"Computing/Solaris/E36856.pdf", 
////"Computing/Solaris/E36858.pdf", 
////"Computing/Solaris/E36824.pdf", 
////"Computing/Solaris/E36797.pdf", 
////"Computing/Solaris/E36816.pdf", 
////"Computing/Solaris/E48546.pdf", 
////"Computing/Solaris/E37476.pdf", 
////"Computing/Solaris/E37630.pdf", 
////"Computing/Solaris/E36863.pdf", 
////"Computing/Solaris/E36818.pdf", 
////"Computing/Solaris/E39021.pdf", 
////"Computing/Solaris/E36864.pdf", 
////"Computing/Shell/Bash Reference Manual.pdf", 
////"Computing/Shell/Linux Command Line and Shell Scripting Bible.pdf", 
////"Computing/Shell/Classic Shell Scripting.pdf", 
////"Computing/Shell/The Z Shell Manual.pdf", 
////"Computing/Shell/Linux Shell Scripting with Bash.pdf", 
////"Computing/Shell/Bash Reference Manual v4.1.pdf", 
////"Computing/Shell/Expert Shell Scripting.pdf", 
////"Computing/Shell/Shell Scripting.pdf", 
////"Computing/Shell/Bash Cookbook.pdf", 
////"Computing/Shell/Bash Quick Reference.pdf", 
////"Computing/Shell/Learning zsh.pdf", 
////"Computing/Shell/Learning the bash Shell - Unix Shell Programming.pdf", 
////"Computing/Shell/805-3917.pdf", 
////"Computing/Shell/Learning the bash Shell.pdf", 
////"Computing/Shell/kornshell.pdf", 
////"Computing/Shell/From Bash to Z Shell.pdf", 
////"Computing/Shell/Mastering UNIX Shell Scripting.pdf", 
////"Computing/Shell/Introduction to the Command Line, 2nd Ed..pdf", 
////"Computing/Vi/Practical Vim.pdf", 
////"Computing/Vi/Modern Vim.pdf", 
////"Computing/Vi/Learning the vi and Vim Editors.pdf", 
////"Computing/Vi/Learning the vi and Vim Editors, 7e.pdf", 
////"Computing/Unlock Complex and Streaming Data with Declarative Data Pipelines.pdf", 
////"Computing/Ansible/ansible_rhel.pdf", 
////"Computing/Ansible/Ansible.pdf", 
////"Computing/OpenShift for Developers.pdf", 
////"Computing/Modern Operating System.pdf", 
////"Computing/OpenSolaris/819-2380.pdf", 
////"Computing/OpenSolaris/816-1681.pdf", 
////"Computing/OpenSolaris/820-1691.pdf", 
////"Computing/OpenSolaris/819-3194.pdf", 
////"Computing/OpenSolaris/817-4415.pdf", 
////"Computing/OpenSolaris/816-1435.pdf", 
////"Computing/OpenSolaris/816-5138.pdf", 
////"Computing/OpenSolaris/820-0643.pdf", 
////"Computing/OpenSolaris/819-1634.pdf", 
////"Computing/OpenSolaris/820-3070.pdf", 
////"Computing/OpenSolaris/817-2543.pdf", 
////"Computing/OpenSolaris/806-6829.pdf", 
////"Computing/OpenSolaris/819-4323.pdf", 
////"Computing/OpenSolaris/820-2429.pdf", 
////"Computing/OpenSolaris/819-3321.pdf", 
////"Computing/OpenSolaris/816-5137.pdf", 
////"Computing/OpenSolaris/819-2724.pdf", 
////"Computing/OpenSolaris/819-2789.pdf", 
////"Computing/OpenSolaris/819-2723.pdf", 
////"Computing/OpenSolaris/817-5477.pdf", 
////"Computing/OpenSolaris/819-2379.pdf", 
////"Computing/OpenSolaris/819-3000.pdf", 
////"Computing/OpenSolaris/820-4680.pdf", 
////"Computing/OpenSolaris/819-3196.pdf", 
////"Computing/OpenSolaris/817-2271.pdf", 
////"Computing/OpenSolaris/819-2450.pdf", 
////"Computing/OpenSolaris/819-7761.pdf", 
////"Computing/OpenSolaris/819-2145.pdf", 
////"Computing/OpenSolaris/819-3159.pdf", 
////"Computing/OpenSolaris/817-0366.pdf", 
////"Computing/OpenSolaris/820-0696.pdf", 
////"Computing/OpenSolaris/817-0406.pdf", 
////"Computing/OpenSolaris/819-0690.pdf", 
////"Computing/OpenSolaris/819-6990.pdf", 
////"Computing/NetBSD/NetBSD Guide 9.3.pdf", 
////"Computing/NetBSD/NetBSD Guide 9.2.pdf", 
////"Computing/NetBSD/NetBSD Guide 9.1.pdf", 
////"Computing/NetBSD/The NetBSD Guide.pdf", 
////"Computing/NetBSD/NetBSD Guide 8.1.pdf", 
////"Computing/NetBSD/NetBSD Guide 9.0.pdf", 
////"Computing/NetBSD/NetBSD Guide 8.0.pdf", 
////"Computing/CentOS/CentOS Bible.pdf", 
////"Computing/Solaris 10 - The Complete Reference.pdf", 
////"Databases/OreillyGraphDatabases.pdf", 
////"Databases/OrientDB/Object API | OrientDB Manual.pdf", 
////"Databases/OrientDB/Document API  OrientDB Manual.pdf", 
////"Databases/OrientDB/Object API  OrientDB Manual.pdf", 
////"Databases/OrientDB/Graph API  OrientDB Manual.pdf", 
////"Databases/OrientDB/Document API | OrientDB Manual.pdf", 
////"Databases/OrientDB/Graph API | OrientDB Manual.pdf", 
////"Databases/Graph Databases.pdf", 
////"Databases/Neo4j/Graph Algorithms - Practical Examples in Apache Spark and Neo4j.pdf", 
////"Development/Jenkins/Learning Jenskins.pdf", 
////"Development/Jenkins/Jenkins User Handbook.pdf", 
////"Development/Jenkins/Jenkins The Definitive Guide.pdf", 
////"Development/C/c99-standarden.pdf", 
////"Development/C/ooc.pdf", 
////"Development/C/Turbo_C_Version_2.0_Reference_Guide_1988.pdf", 
////"Development/C/The C programming language.pdf", 
////"Development/Pro Git.pdf", 
////"Development/Full Stack Testing.pdf", 
////"Development/PHP/PHP Reference.pdf", 
////"Development/PHP/PHP in a Nutshell.pdf", 
////"Development/Pascal/Turbo_Pascal_Version_6.0_Users_Guide_1990.pdf", 
////"Development/Pascal/Turbo_Pascal_Version_6.0_Library_Ref_1990.pdf", 
////"Development/Pascal/Turbo_Pascal_Version_6.0_Turbo_Vision_1990.pdf", 
////"Development/Pascal/Turbo_Pascal_Version_6.0_Programmers_Guide.pdf", 
////"Development/JavaScript/Web Development with Node and Express.pdf", 
////"Development/JavaScript/JavaScript The Definitive Guide.pdf", 
////"Development/Software Architecture Patterns.pdf", 
////"Development/The Path to GitOps.pdf", 
////"Development/Python/Async IO in Python: A Complete Walkthrough – Real Python.pdf", 
////"Development/Python/Python Cookbook.pdf", 
////"Development/Python/Fluent Python.pdf", 
////"Development/Python/Programming Python.pdf", 
////"Development/Python/Think Python, 2.0.17.pdf", 
////"Development/Python/Async IO in Python A Complete Walkthrough – Real Python.pdf", 
////"Development/Python/Fundamentals of Python Programming.pdf", 
////"Development/Python/How to Make Python Statically Typed — The Essential Guide | by Dario Radečić | Towards Data Science.pdf", 
////"Development/Python/Learning Python.pdf", 
////"Development/Python/Python Data Science Handbook.pdf", 
////"Development/Python/Think Python 2nd.pdf", 
////"Development/Python/PyTorch/PyTorch Tensors and autograd — PyTorch Tutorials 1.8.1+cu102 documentation.pdf", 
////"Development/Python/PyTorch/Neural Networks — PyTorch Tutorials 1.8.1+cu102 documentation.pdf", 
////"Development/Python/PyTorch/PyTorch Tensors — PyTorch Tutorials 1.8.1+cu102 documentation.pdf", 
////"Development/Python/PyTorch/PyTorch Defining New autograd Functions — PyTorch Tutorials 1.8.1+cu102 documentation.pdf", 
////"Development/Python/PyTorch/PyTorch: Tensors — PyTorch Tutorials 1.8.1+cu102 documentation.pdf", 
////"Development/Python/PyTorch/Warm-up numpy — PyTorch Tutorials 1.8.1+cu102 documentation.pdf", 
////"Development/Python/PyTorch/PyTorch: Tensors and autograd — PyTorch Tutorials 1.8.1+cu102 documentation.pdf", 
////"Development/Python/PyTorch/Anomaly Detection with AutoEncoder (pytorch).pdf", 
////"Development/Python/PyTorch/PyTorch: Defining New autograd Functions — PyTorch Tutorials 1.8.1+cu102 documentation.pdf", 
////"Development/Python/PyTorch/Warm-up: numpy — PyTorch Tutorials 1.8.1+cu102 documentation.pdf", 
////"Development/Python/Mastering Python, Second Edition.pdf", 
////"Development/Python/Build a GraphQL API with Subscriptions using Python, Asyncio and Ariadne.pdf", 
////"Development/Python/Deep Learning with PyTorch.pdf", 
////"Development/Version Control with Subversion.pdf", 
////"Development/HTML/html5-cheat-sheet.pdf", 
////"Development/HTML/css3-cheat-sheet.pdf", 
////"Development/Guile/guile-3.0.9.pdf", 
////"Development/Guile/guile-2.2.6.pdf", 
////"Development/Guile/guile-3.0.5.pdf", 
////"Development/Ant The Definitive Guide.pdf", 
////"Development/C++/C++ Primer 5th Edition.pdf", 
////"Development/C++/Borland Turbo Vision for C++ Users Guide.pdf", 
////"Development/C++/Stroustrup_book.pdf", 
////"Development/C++/The C++ Programming Language 4th Edition.pdf", 
////"Development/C++/C++ Primer 3rd edition.pdf", 
////"Development/Java/Java in a Nutshell, java 1.4.pdf", 
////"Development/Java/Java Threads.pdf", 
////"Development/Java/Core Java 2, Volume 1 - ISBN.pdf", 
////"Development/Java/Think Java, 5.1.2.pdf", 
////"Development/Java/Think Java, 2ed 7.1.0.pdf", 
////"Development/Java/Think Java, 6.1.3.pdf", 
////"Development/Java/Java IO.pdf", 
////"Development/Java/Learning Java.pdf", 
////"Development/Java/Java in a Nutshell, java 1.8.pdf", 
////"Development/VS2003_General_en-us.pdf", 
////"Development/Managing Projects with GNU Make.pdf", 
////"Development/BASIC/Applesoft II BASIC Programming Reference Manual.pdf", 
////"Development/BASIC/BASIC_Language_Reference_Manual_Mar80.pdf", 
////"Development/BASIC/Yabasic.pdf", 
////"Development/BASIC/STOS_User_Guide.pdf", 
////"Development/BASIC/BASIC_Oct64.pdf", 
////"Development/BASIC/atari-stos-manual.pdf", 
////"Development/Scheme/s48manual.pdf", 
////"Development/Scheme/s48-user-guide.txt.pdf", 
////"Development/Scheme/mit-scheme-user.pdf", 
////"Development/Scheme/guile.pdf", 
////"Development/Ten Things to Know About ModelOps.pdf", 
////    	};
////
////    	for( String book : books ) {
////    		if( book != null ) {
////    			String filePath = "/tmp/Books/" + book;
////    			System.out.println("Book:" + filePath);
////    			File file = new File(filePath);
////    	        if (file.exists()) {
////    	            System.out.println("The file exists.");
////    	            Resource resource = new FileSystemResource(file);
////    	            TikaDocumentReader documentReader = new TikaDocumentReader(resource);
////    	        	List<Document> documents = documentReader.get();
////    	        	// for( Document document : documents ) {
////    	        	// 	System.out.println(" >> DOC: " + document.getId());
////    	        	// }
////    	        	for( Document largeDocument : documents ) {
////    	        		// TokenTextSplitter textSplitter = new TokenTextSplitter(500, 100,5,1000,true);
////        	        	TokenTextSplitter textSplitter = new TokenTextSplitter();
////        	        	// List<Document> splitDocuments = textSplitter.apply(documents);
////        	        	List<Document> chunkedDocuments = textSplitter.split(largeDocument);
////        	        	// for( Document document : splitDocuments ) {
////        	        	// 	System.out.println(" >> SDOC: " + document.getId());
////        	        	// }
////        	        	// vectorStore.add(splitDocuments);
////        	        	vectorStore.accept(chunkedDocuments);
////        	        	// vectorStore.
////        	        	// SearchRequest.builder().query(question.question()).build();
////        	        	// List<Document> relevantDocuments = vectorStore.similaritySearch(SearchRequest.builder().query(question.question()).build()); // .withTopK(5));
////        	        	// for( Document document : relevantDocuments ) {
////        	        	// 	System.out.println(" >> RDOC: " + document.getId());
////        	        	// }
////    	        	}
////    	        } else {
////    	            System.out.println("The file does not exist.");
////    	        }
////    		}
////    	}
//
//        ChatResponse response = customOllamaChatClient.prompt()
//                // .advisors(new QuestionAnswerAdvisor(vectorStore))
//                .advisors(QuestionAnswerAdvisor.builder(vectorStore).build()) // Build and add the advisor
//                .user(question.question())
//                .call()
//                .chatResponse();
//
//        if(response != null) {
//            String answer = response.getResult().getOutput().getText();
//            return new Answer(answer);
//        }
//
//        return new Answer("Sorry, I couldn't find an answer to your question.");
//
//    }
//}
