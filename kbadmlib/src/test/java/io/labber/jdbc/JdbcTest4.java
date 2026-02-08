
// 
// Copyright (c) 2024, 2025, John Grundback
// All rights reserved.
// 

package io.labber.jdbc;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import io.labber.kbadm.KBException;
import io.labber.kbadm.model.Chunk;
import io.labber.kbadm.model.Document;
import io.labber.kbadm.model.Metadata;
import io.labber.kbadm.model.Status;
import io.labber.kbadm.model.rowmapper.ChunkRowMapper;
import io.labber.kbadm.model.rowmapper.DocumentRowMapper;
import io.labber.kbadm.validator.DocumentValidator;

/**
 * 
 * @author john
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
// public class JdbcTest4 extends TestCase {
public class JdbcTest4 {

	@Configuration
	static class ContextConfiguration {

		// Bean definitions go here.

		@Bean
		public DataSource dataSource() {

			DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
			dataSourceBuilder.driverClassName("org.postgresql.Driver");
			dataSourceBuilder.url("jdbc:postgresql://10.88.88.197:5432/pgvector_db");
			dataSourceBuilder.username("postgres");
			dataSourceBuilder.password("postgres");

			return dataSourceBuilder.build();
		}

		@Bean
		public JdbcTemplate jdbcTemplate() {
			return new JdbcTemplate(this.dataSource());
		}

	}

	// Bean references go here.

	@Autowired
	DataSource dataSource;

	@Autowired
	JdbcTemplate jdbcTemplate;

	/**
	 * Create the test case
	 *
	 * @param testName name of the test case
	 */
	// public JdbcTest(String testName) {
	// 	super(testName);
	// }

	/**
	 * @return the suite of tests being tested
	 */
	// public static Test suite() {
	// 	return new TestSuite(JdbcTest.class);
	// }

	/**
	 * 
	 */
	// public void setUp() {
	// 	// Configurator.setAllLevels("", Level.ALL); 
	// }

	// @Before
	// public void setUp() {
	// 	Configurator.setAllLevels("", Level.ALL);
	// }

	/**
	 * 
	 * @param resultSet
	 * @return
	 * @throws SQLException
	 */
	protected Collection<Map<String, Object>> convert(ResultSet resultSet) throws SQLException {

		// long startTime = System.currentTimeMillis();

		ResultSet contentrs = resultSet;
		ResultSetMetaData contentrsmd = contentrs.getMetaData();

		Collection<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
		while (contentrs.next()) {

			Map<String, Object> row = null;
			row = new HashMap<String, Object>();

			for (int i = 1; i <= contentrsmd.getColumnCount(); i++) {
				row.put(contentrsmd.getColumnName(i), contentrs.getObject(i));
			}

			rows.add(row);
		}

		// String pctx = "convert";
		// this.fnPerformanceTiming(pctx, startTime, state);

		return rows;
	}

	/**
	 * 
	 * @param statment
	 * @return
	 * @throws SQLException
	 */
	protected Collection<Map<String, Object>> runStatement(String statment) throws SQLException {

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Collection<Map<String, Object>> rsdata = null;
		try {
			conn = this.jdbcTemplate.getDataSource().getConnection();
			stmt = conn.prepareStatement(statment);
			rs = stmt.executeQuery();
			rsdata = this.convert(rs);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if( rs != null ) {
				try { rs.close(); } catch (SQLException e) { /* log error */ }
			}
			if( stmt != null ) {
				try { stmt.close(); } catch (SQLException e) { /* log error */ }
			}
			if( conn != null ) {
				try { conn.close(); } catch (SQLException e) { /* log error */ }
			}
		}

		return rsdata;
	}

	/**
	 * 
	 * @param statment
	 * @return
	 * @throws SQLException
	 */
	protected Collection<Chunk> runChunkStatement(String statment) throws SQLException {

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Collection<Chunk> list = null;
		try {

			JdbcTemplate tmpl = this.jdbcTemplate;
			PreparedStatementCreator psc = new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement ps = con.prepareCall(statment);
					return ps;
				}
			};

			list = tmpl.query(psc, new ChunkRowMapper());

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if( rs != null ) {
				try { rs.close(); } catch (SQLException e) { /* log error */ }
			}
			if( stmt != null ) {
				try { stmt.close(); } catch (SQLException e) { /* log error */ }
			}
			if( conn != null ) {
				try { conn.close(); } catch (SQLException e) { /* log error */ }
			}
		}

		return list;
	}

	/**
	 * 
	 * @param statment
	 * @return
	 * @throws SQLException
	 */
	protected Collection<Document> runDocumentStatement(String statment) throws SQLException {

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Collection<Document> list = null;
		try {

			JdbcTemplate tmpl = this.jdbcTemplate;
			PreparedStatementCreator psc = new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement ps = con.prepareCall(statment);
					return ps;
				}
			};

			list = tmpl.query(psc, new DocumentRowMapper());

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if( rs != null ) {
				try { rs.close(); } catch (SQLException e) { /* log error */ }
			}
			if( stmt != null ) {
				try { stmt.close(); } catch (SQLException e) { /* log error */ }
			}
			if( conn != null ) {
				try { conn.close(); } catch (SQLException e) { /* log error */ }
			}
		}

		return list;
	}

	public String[] getDocumentNames() {

		String[] books = {
"FreeBSD 13.0 Handbook.pdf", 
"FreeBSD 11.4 Developers Handbook.pdf", 
"O'Reilly - The Complete FreeBSD.pdf", 
"FreeBSD 14.2 Handbook.pdf", 
"FreeBSD 10.3 FAQ.pdf", 
"FreeBSD 11.4 Architecture Handbook.pdf", 
"FreeBSD 14.0 Handbook.pdf", 
"FreeBSD 13.2 Handbook.pdf", 
"FreeBSD 14.0 Developers Handbook.pdf", 
"FreeBSD 13.2 Architecture Handbook.pdf", 
//"FreeBSD 14.2 Developers Handbook.pdf", .1
"FreeBSD 13.2 Developers Handbook.pdf", 
"FreeBSD 14.2 FAQ.pdf", 
//"FreeBSD 14.2 Handbook.pdf", .1
"FreeBSD 11.4 Handbook.pdf", 
"O'Reilly - The Complete FreeBSD, 4th ed.pdf", 
"FreeBSD 14.0 Architecture Handbook.pdf", 
"FreeBSD 13.2 FAQ.pdf", 
"FreeBSD 10.3 Architecture Handbook.pdf", 
"FreeBSD 12.2 FAQ.pdf", 
"FreeBSD 14.0 FAQ.pdf", 
"FreeBSD 13.0 Developers Handbook.pdf", 
"The Design and Implementation of the 4.4BSD Operating System.pdf", 
"BSD UNIX Toolbox 1000+ Commands for FreeBSD, OpenBSD and NetBSD.pdf", 
"FreeBSD 10.3 Handbook.pdf", 
"FreeBSD 13.0 Architecture Handbook.pdf", 
"FreeBSD 11.4 FAQ.pdf", 
"The Complete FreeBSD, 4th Edition.pdf", 
"FreeBSD 13.0 FAQ.pdf", 
"FreeBSD 12.2 Developers Handbook.pdf", 
"The Complete FreeBSD 4ed.pdf", 
"FreeBSD 14.2 Developers Handbook.pdf", 
"FreeBSD 10.3 Developers Handbook.pdf", 
"FreeBSD 12.2 Architecture Handbook.pdf", 
"FreeBSD 12.2 Handbook.pdf", 
"FreeBSD 14.2 Architecture Handbook.pdf", 
"Absolute FreeBSD.pdf", 
"UNIX Programmers Manual - 2ed.pdf", 
"unixinanutshell_4thedition.pdf", 
"UNIX Programmers Manual - 4.2BSD 2C.pdf", 
"A Commentary on the Sixth Edition Unix Operating System.pdf", 
"UNIX Programmers Manual - 1ed.pdf", 
"The Design Of the Unix Operating System.pdf", 
"The Design of Unix Operating System.pdf", 
"UNIX Programmers Manual - 6ed.pdf", 
"Mastering Unix.pdf", 
"Unix Operating System Source Code Level 6.pdf", 
"The UNIX-HATERS Handbook.pdf", 
"Unix.in.a.Nutshell.pdf", 
"UNIX Programming Environment.pdf", 
"UNIX and Linux System Administration Handbook, 4ed.pdf", 
"UNIX Programmers Manual - 4ed.pdf", 
"Beginning Unix.pdf", 
"A COMMENTARY ON THE SIXTH EDITION UNIX OPERATING SYSTEM.pdf", 
"UNIX and Linux System Administration Handbook, 5ed.pdf", 
"UNIX Programmers Manual - 5ed.pdf", 
"PreliminaryUnixImplementationDocument_Jun72.pdf", 
"An Introduction To Neural Networks.pdf", 
"Beginning Regular Expressions [Watt 2005-02-04].pdf", 
"Regular Expressions Cookbook_ Detailed Solutions in Eight Programming Languages (2nd ed.) [Goyvaerts & Levithan 2012-09-06].pdf", 
"Regular Expression Pocket Reference_ Regular Expressions for Perl, Ruby, PHP, Python, C, Java, and .NET (2nd ed.) [Stubblebine 2007-07-28].pdf", 
"Introducing Regular Expressions_ Unraveling Regular Expressions, Step-by-Step [Fitzgerald 2012-08-03].pdf", 
"Mastering Regular Expressions_ Powerful Techniques for Perl and Other Tools (2nd ed.) [Friedl 2002-07-15].pdf", 
"Debian Reference 2007.pdf", 
"The Debian System Concepts And Techniques.pdf", 
"Debian Reference 2021.pdf", 
"The Debian GNU Linux FAQ.pdf", 
"The Debian Administrators Handbook.pdf", 
"Modern Operating System - Tanenbaum.pdf", 
"Red Hat Linux 5.2 Installation Guide.pdf", 
"Red Hat Enterprise Linux 5 Administration Unleashed.pdf", 
"Red Hat Enterprise Linux 8 Security hardening.pdf", 
"Red Hat Enterprise Linux 8 Managing and monitoring security updates.pdf", 
"Beginning Red Hat Linux 9.pdf", 
"Red Hat Universal Base Images.pdf", 
"Red Hat Enterprise Linux 6 Administration.pdf", 
"Red Hat Enterprise Linux 8 Securing networks.pdf", 
"Red Hat Enterprise Linux 7 SELinux Users and Administrators Guide.pdf", 
"Red Hat Virtualization 4.4 REST API Guide.pdf", 
"Red Hat Enterprise Linux 7 System Administrators Guide.pdf", 
"Red_Hat_Linux_Complete_Reference.pdf", 
"Red Hat Enterprise Linux 7 Security Guide.pdf", 
"Red Hat Enterprise Linux 8 Using SELinux.pdf", 
"ma-linux-management-ebook-f18267-201912-en.pdf", 
"emacs-29.2.pdf", 
"Learning GNU Emacs.pdf", 
"emacs-28.2.pdf", 
"emacs-26.3.pdf", 
"emacs-24.4.pdf", 
"emacs.pdf", 
"emacs-30.1.pdf", 
"Genera_Concepts.pdf", 
"Sed & Awk.pdf", 
"The_AWK_Programming_Language_-_Aho,_Weinberger,_Kernighan.pdf", 
"ZFS.pdf", 
"ZFS2.pdf", 
"Kubernetes Native Microservices.pdf", 
"Cluster API and Declarative Kubernetes Management.pdf", 
"Knative Cookbook.pdf", 
"Kubernetes Patterns.pdf", 
"Production Kubernetes.pdf", 
"FreeNAS 11.2-U3 User Guide.pdf", 
"TrueNAS-11.3-U5-User-Guide_screen.pdf", 
"FreeNAS 11.3-U5 User Guide.pdf", 
"CORE12.0Docs.pdf", 
"Linux - The Complete Reference.pdf", 
"Linux - The Complete Reference, 6 Ed.pdf", 
"Beginning Linux Programming, 4th Edition.pdf", 
"Linux System Programming.pdf", 
"Gentoo Linux amd64 Handbook: Installing Gentoo - Gentoo Wiki.pdf", 
"Gentoo Linux amd64 Handbook Installing Gentoo - Gentoo Wiki.pdf", 
"Linux Administration Made Easy.pdf", 
"Linux Network Administrators Guide 2.pdf", 
"Running_Linux_4th_Edition.pdf", 
"The Linux Users Guide.pdf", 
"Linux In A Nutshell 6th Edition.pdf", 
"Linux From Scratch 6.1.1.pdf", 
"Linux Bible 8th Edition.pdf", 
"Linux Kernel Module Programming Guide 1.1.0 2.pdf", 
"The Linux Programmers Guide.pdf", 
"Linux Installation and Getting Started 3.2.pdf", 
"Linux Kernel Module Programming Guide 1.1.0 1.pdf", 
"Learning-Modern-Linux-A-Handbook-for-the-Cloud-Native-Practitioner-by-Michael-Hausenblas_bibis.ir.pdf", 
"Running Linux - Matthias Kalle Dalheimer and Matt Welsh.pdf", 
"Linux Complete Command Reference.pdf", 
"The Linux System Administrators Guide 0.6.2.pdf", 
"Linux Kernel 2.4 Internals.pdf", 
"SELinux_Notebook.pdf", 
"Kali Linux Revealed.pdf", 
"Linux From Scratch.pdf", 
"The Linux Network Administrators Guide 1.0.pdf", 
"The Linux Programmers Guide__.pdf", 
"Linux Bible 2010 Edition.pdf", 
"Linux From Scratch 7.7.pdf", 
"Securing-Optimizing-Linux-RH-Edition-v1.3.pdf", 
"Smart Home Automation with Linux.pdf", 
"Medley-Primer.pdf", 
"IRM.pdf", 
"SunUserGuide.pdf", 
"Genera_User_s_Guide.pdf", 
"Slackware Desktop Guide.pdf", 
"Slackware Linux Essentials.pdf", 
"slackpkg.pdf", 
"Configure your new Slackware System.pdf", 
"Enabling Sudo on Slackware.pdf", 
"What Is SRE.pdf", 
"Modern Operating Systems 2nd Ed by Tanenbaum (with pdf index).pdf", 
"Absolute OpenBSD - Unix For The Practical Paranoid (2003).pdf", 
"Absolute OpenBSD.pdf", 
"Ubuntu Server Guide 16.04.pdf", 
"Mastering Ubuntu Server.pdf", 
"Installing Ubuntu Server.pdf", 
"Ubuntu Unleashed 2019 Edition.pdf", 
"Ubuntu Server Guide 18.04.pdf", 
"Ubuntu Server Guide 20.04.pdf", 
"E39134.pdf", 
"E36855.pdf", 
"E36829.pdf", 
"E37516.pdf", 
"E36852.pdf", 
"E36820.pdf", 
"E36827.pdf", 
"E36812.pdf", 
"E36815.pdf", 
"E36869.pdf", 
"E37475.pdf", 
"E36867.pdf", 
"E36860.pdf", 
"E49624.pdf", 
"E37121.pdf", 
"E36804.pdf", 
"E37126.pdf", 
"E36803.pdf", 
"E38524.pdf", 
"E36836.pdf", 
"E36831.pdf", 
"E36843.pdf", 
"E36844.pdf", 
"E36838.pdf", 
"E36861.pdf", 
"E36866.pdf", 
"E36868.pdf", 
"E37474.pdf", 
"E54155.pdf", 
"E37473.pdf", 
"E36813.pdf", 
"E36826.pdf", 
"E36821.pdf", 
"E37517.pdf", 
"E36853.pdf", 
"E36828.pdf", 
"E36845.pdf", 
"E36842.pdf", 
"E36830.pdf", 
"E38254.pdf", 
"E36837.pdf", 
"E52463.pdf", 
"E37127.pdf", 
"E36802.pdf", 
"E36805.pdf", 
"E36841.pdf", 
"E36846.pdf", 
"E36848.pdf", 
"E36834.pdf", 
"E37123.pdf", 
"E36806.pdf", 
"E37629.pdf", 
"E54953.pdf", 
"E37124.pdf", 
"E36801.pdf", 
"E36819.pdf", 
"E36865.pdf", 
"E37631.pdf", 
"E36862.pdf", 
"E39499.pdf", 
"E36817.pdf", 
"E36822.pdf", 
"E39067.pdf", 
"E36859.pdf", 
"E36825.pdf", 
"E36857.pdf", 
"E36850.pdf", 
"E37125.pdf", 
"E36800.pdf", 
"E37122.pdf", 
"E36807.pdf", 
"E37628.pdf", 
"E36832.pdf", 
"E36849.pdf", 
"E36835.pdf", 
"E36847.pdf", 
"E36840.pdf", 
"E36799.pdf", 
"E36851.pdf", 
"E36856.pdf", 
"E36858.pdf", 
"E36824.pdf", 
"E36797.pdf", 
"E36816.pdf", 
"E48546.pdf", 
"E37476.pdf", 
"E37630.pdf", 
"E36863.pdf", 
"E36818.pdf", 
"E39021.pdf", 
"E36864.pdf", 
"Bash Reference Manual.pdf", 
"Linux Command Line and Shell Scripting Bible.pdf", 
"Classic Shell Scripting.pdf", 
"The Z Shell Manual.pdf", 
"Linux Shell Scripting with Bash.pdf", 
"Bash Reference Manual v4.1.pdf", 
"Expert Shell Scripting.pdf", 
"Shell Scripting.pdf", 
"Bash Cookbook.pdf", 
"Bash Quick Reference.pdf", 
"Learning zsh.pdf", 
"Learning the bash Shell - Unix Shell Programming.pdf", 
"805-3917.pdf", 
"Learning the bash Shell.pdf", 
"kornshell.pdf", 
"From Bash to Z Shell.pdf", 
"Mastering UNIX Shell Scripting.pdf", 
"Introduction to the Command Line, 2nd Ed..pdf", 
"Practical Vim.pdf", 
"Modern Vim.pdf", 
"Learning the vi and Vim Editors.pdf", 
"Learning the vi and Vim Editors, 7e.pdf", 
"Unlock Complex and Streaming Data with Declarative Data Pipelines.pdf", 
"ansible_rhel.pdf", 
"Ansible.pdf", 
"OpenShift for Developers.pdf", 
"Modern Operating System.pdf", 
"819-2380.pdf", 
"816-1681.pdf", 
"820-1691.pdf", 
"819-3194.pdf", 
"817-4415.pdf", 
"816-1435.pdf", 
"816-5138.pdf", 
"820-0643.pdf", 
"819-1634.pdf", 
"820-3070.pdf", 
"817-2543.pdf", 
"806-6829.pdf", 
"819-4323.pdf", 
"820-2429.pdf", 
"819-3321.pdf", 
"816-5137.pdf", 
"819-2724.pdf", 
"819-2789.pdf", 
"819-2723.pdf", 
"817-5477.pdf", 
"819-2379.pdf", 
"819-3000.pdf", 
"820-4680.pdf", 
"819-3196.pdf", 
"817-2271.pdf", 
"819-2450.pdf", 
"819-7761.pdf", 
"819-2145.pdf", 
"819-3159.pdf", 
"817-0366.pdf", 
"820-0696.pdf", 
"817-0406.pdf", 
"819-0690.pdf", 
"819-6990.pdf", 
"NetBSD Guide 9.3.pdf", 
"NetBSD Guide 9.2.pdf", 
"NetBSD Guide 9.1.pdf", 
"The NetBSD Guide.pdf", 
"NetBSD Guide 8.1.pdf", 
"NetBSD Guide 9.0.pdf", 
"NetBSD Guide 8.0.pdf", 
"CentOS Bible.pdf", 
"Solaris 10 - The Complete Reference.pdf", 
"OreillyGraphDatabases.pdf", 
"Object API | OrientDB Manual.pdf", 
"Document API  OrientDB Manual.pdf", 
"Object API  OrientDB Manual.pdf", 
"Graph API  OrientDB Manual.pdf", 
"Document API | OrientDB Manual.pdf", 
"Graph API | OrientDB Manual.pdf", 
"Graph Databases.pdf", 
"Graph Algorithms - Practical Examples in Apache Spark and Neo4j.pdf", 
"Learning Jenskins.pdf", 
"Jenkins User Handbook.pdf", 
"Jenkins The Definitive Guide.pdf", 
"c99-standarden.pdf", 
"ooc.pdf", 
"Turbo_C_Version_2.0_Reference_Guide_1988.pdf", 
"The C programming language.pdf", 
"Pro Git.pdf", 
"Full Stack Testing.pdf", 
"PHP Reference.pdf", 
"PHP in a Nutshell.pdf", 
"Turbo_Pascal_Version_6.0_Users_Guide_1990.pdf", 
"Turbo_Pascal_Version_6.0_Library_Ref_1990.pdf", 
"Turbo_Pascal_Version_6.0_Turbo_Vision_1990.pdf", 
"Turbo_Pascal_Version_6.0_Programmers_Guide.pdf", 
"Web Development with Node and Express.pdf", 
"JavaScript The Definitive Guide.pdf", 
"Software Architecture Patterns.pdf", 
"The Path to GitOps.pdf", 
"Async IO in Python: A Complete Walkthrough – Real Python.pdf", 
"Python Cookbook.pdf", 
"Fluent Python.pdf", 
"Programming Python.pdf", 
"Think Python, 2.0.17.pdf", 
"Async IO in Python A Complete Walkthrough – Real Python.pdf", 
"Fundamentals of Python Programming.pdf", 
"How to Make Python Statically Typed — The Essential Guide | by Dario Radečić | Towards Data Science.pdf", 
"Learning Python.pdf", 
"Python Data Science Handbook.pdf", 
"Think Python 2nd.pdf", 
"PyTorch Tensors and autograd — PyTorch Tutorials 1.8.1+cu102 documentation.pdf", 
"Neural Networks — PyTorch Tutorials 1.8.1+cu102 documentation.pdf", 
"PyTorch Tensors — PyTorch Tutorials 1.8.1+cu102 documentation.pdf", 
"PyTorch Defining New autograd Functions — PyTorch Tutorials 1.8.1+cu102 documentation.pdf", 
"PyTorch: Tensors — PyTorch Tutorials 1.8.1+cu102 documentation.pdf", 
"Warm-up numpy — PyTorch Tutorials 1.8.1+cu102 documentation.pdf", 
"PyTorch: Tensors and autograd — PyTorch Tutorials 1.8.1+cu102 documentation.pdf", 
"Anomaly Detection with AutoEncoder (pytorch).pdf", 
"PyTorch: Defining New autograd Functions — PyTorch Tutorials 1.8.1+cu102 documentation.pdf", 
"Warm-up: numpy — PyTorch Tutorials 1.8.1+cu102 documentation.pdf", 
"Mastering Python, Second Edition.pdf", 
"Build a GraphQL API with Subscriptions using Python, Asyncio and Ariadne.pdf", 
"Deep Learning with PyTorch.pdf", 
"Version Control with Subversion.pdf", 
"html5-cheat-sheet.pdf", 
"css3-cheat-sheet.pdf", 
"guile-3.0.9.pdf", 
"guile-2.2.6.pdf", 
"guile-3.0.5.pdf", 
"Ant The Definitive Guide.pdf", 
"C++ Primer 5th Edition.pdf", 
"Borland Turbo Vision for C++ Users Guide.pdf", 
"Stroustrup_book.pdf", 
"The C++ Programming Language 4th Edition.pdf", 
"C++ Primer 3rd edition.pdf", 
"Java in a Nutshell, java 1.4.pdf", 
"Java Threads.pdf", 
"Core Java 2, Volume 1 - ISBN.pdf", 
"Think Java, 5.1.2.pdf", 
"Think Java, 2ed 7.1.0.pdf", 
"Think Java, 6.1.3.pdf", 
"Java IO.pdf", 
"Learning Java.pdf", 
"Java in a Nutshell, java 1.8.pdf", 
"VS2003_General_en-us.pdf", 
"Managing Projects with GNU Make.pdf", 
"Applesoft II BASIC Programming Reference Manual.pdf", 
"BASIC_Language_Reference_Manual_Mar80.pdf", 
"Yabasic.pdf", 
"STOS_User_Guide.pdf", 
"BASIC_Oct64.pdf", 
"atari-stos-manual.pdf", 
"s48manual.pdf", 
"s48-user-guide.txt.pdf", 
"mit-scheme-user.pdf", 
"guile.pdf", 
"Ten Things to Know About ModelOps.pdf", 
		};

		return books;
	}

	public Document checkDocument(String document) throws SQLException, KBException {

		String documentName = document;
		String documentId = null;

		Collection<Chunk> list = this.runChunkStatement(
			"SELECT * FROM vector_store WHERE metadata->>'source' = '" + document.replace("'", "''") + "';"
		);

		if( (list != null) && (!list.isEmpty()) ) {
			documentId = new Metadata(list.iterator().next().getMetadata()).getParentDocumentId();
		}

		DocumentValidator validator = new DocumentValidator(
			new Document().withName(documentName).withDescription(documentName).withId(documentId), 
			list
		);

		// return 
		Document doc = validator.validate();

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println(" Chunk check for document: " + document + ": " + doc.getStatus().toString() + ", " + doc.getReason().toString() + ", " + dateFormat.format(doc.getTimestamp()));

		Collection<Document> list2 = this.runDocumentStatement("SELECT * FROM document_store WHERE name = '" + document.replace("'", "''") + "' LIMIT 100;");
		if( (list2 != null) && (list2.size() > 0) ) {
			System.out.println(" Document " + document + " already exists, skip insert");
		} else {
			System.out.println(" Document " + document + " does not exist, inserting");
			this.insertDocument(doc, list);
		}

		return doc;
	}

	public void insertDocument(Document document, Collection<Chunk> list) {

		if( document == null ) {
			System.out.println(" Document is invalid, skip insert");
			return;
		}

		if( document.getName() == null ) {
			System.out.println(" Document is invalid, skip insert");
			return;
		}

		if( document.getId() == null ) {
			System.out.println(" Document is invalid, skip insert");
			return;
		}

		if( list == null ) {
			System.out.println(" Document is empty, skip insert");
			return;
		}

		if( list.size() == 0 ) {
			System.out.println(" Document is empty, skip insert");
			return;
		}

		for( Chunk chunk : list ) {

			if( chunk == null ) {
				System.out.println(" Document chunk is invalid, skip insert");
				return;
			}

			if( chunk.getId() == null ) {
				System.out.println(" Document chunk is invalid, skip insert");
				return;
			}

		}

		String SQL_INSERT1 = "INSERT INTO document_store(id, name, description, chunks, total, status, reason, created, modified, timestamp) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try (
			Connection conn = this.jdbcTemplate.getDataSource().getConnection();
			PreparedStatement pstmt = conn.prepareStatement(SQL_INSERT1, Statement.RETURN_GENERATED_KEYS)
		) {

			Date date1 = new Date(System.currentTimeMillis());

			pstmt.setObject(1, UUID.fromString(document.getId()));
			pstmt.setString(2, document.getName());
			pstmt.setString(3, document.getDescription());
			pstmt.setInt(4, document.getChunks());
			pstmt.setInt(5, document.getTotal());
			pstmt.setString(6, document.getStatus());
			pstmt.setString(7, document.getReason());
			pstmt.setDate(8, date1); // document.getCreated());
			pstmt.setDate(9, date1); // document.getModified());
			pstmt.setDate(10, date1); // document.getTimestamp());

			int affectedRows = pstmt.executeUpdate();
			System.out.println(affectedRows + " document insert row(s) affected.");

		} catch (SQLException e) {
			e.printStackTrace();
		}

		for( Chunk chunk : list ) {

			String SQL_INSERT2 = "INSERT INTO document_chunk(document_id, vector_id, timestamp) VALUES(?, ?, ?)";

			try (
				Connection conn = this.jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstmt = conn.prepareStatement(SQL_INSERT2, Statement.RETURN_GENERATED_KEYS)
			) {

				Date date2 = new Date(System.currentTimeMillis());

				pstmt.setObject(1, UUID.fromString(document.getId()));
				pstmt.setObject(2, UUID.fromString(chunk.getId()));
				pstmt.setDate(3, date2); // chunk.getTimestamp());

				int affectedRows = pstmt.executeUpdate();
				System.out.println(affectedRows + " chunk insert row(s) affected.");

			} catch (SQLException e) {
				e.printStackTrace();
			}

		}

	}

	@Test
	public void testSimple5() throws SQLException, KBException {

		String[] documents = this.getDocumentNames();

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		for( String document : documents ) {
			Document check = this.checkDocument(document);
			// if( !check.getStatus().toString().equalsIgnoreCase(Status.COMPLETE.toString()) ) {
			System.out.println(" Chunk check for document: " + document + ": " + check.getStatus().toString() + ", " + check.getReason().toString() + ", " + dateFormat.format(check.getTimestamp()));
			// }
		}

	}

}
