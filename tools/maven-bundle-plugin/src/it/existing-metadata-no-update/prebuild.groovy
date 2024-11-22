File cacheFile = new File( basedir, "target/maven-bundle-plugin/org.apache.felix_maven-bundle-plugin_manifest_xx" )
// pom.xml must be older than cache file
cacheFile.setLastModified( new Date().getTime() )

return true
