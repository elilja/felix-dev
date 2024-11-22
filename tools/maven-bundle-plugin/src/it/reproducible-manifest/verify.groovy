String manifest = new File( basedir, "target/classes/META-INF/MANIFEST.MF" ).text

assert !manifest.contains( "Build-Jdk:" )
assert manifest.contains( "Build-Jdk-Spec:" )

assert !manifest.contains( "Build-By:" )

assert !manifest.contains( "Bnd-LastModified:" )
