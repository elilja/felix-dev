new File(basedir, "target/classes/META-INF/MANIFEST.MF").eachLine() { line ->
    if (line.contains("Import-Package") && !line.equals("Import-Package: javax.servlet")) {
        throw new Exception("Import-Package instruction contains version number from test dependency");
    }
}

