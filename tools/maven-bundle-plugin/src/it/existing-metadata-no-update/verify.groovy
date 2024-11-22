String manifest = new File( basedir, "target/classes/META-INF/MANIFEST.MF" ).text

assert manifest.isEmpty()
