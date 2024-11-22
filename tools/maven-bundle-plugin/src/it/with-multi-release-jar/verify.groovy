String manifest = new File( basedir, "target/classes/META-INF/MANIFEST.MF" ).text
assert !manifest.isEmpty()

manifest.eachLine() { line ->
    if (line.contains("Tool") && !line.contains("7.0.0")) {
        throw new Exception("Wrong bnd version used");
    }
    if (line.contains("Embedded-Artifacts") && !line.contains("jersey-server-3.1.7.jar")) {
        throw new Exception("The multi release jar is not properly embedded");
    }
}


