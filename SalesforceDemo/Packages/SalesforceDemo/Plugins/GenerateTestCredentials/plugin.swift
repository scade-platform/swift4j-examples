import PackagePlugin
import Foundation

@main
struct GenerateTestCredentials: BuildToolPlugin {
    func createBuildCommands(context: PluginContext, target: Target) throws -> [Command] {
        let outputDir = context.pluginWorkDirectoryURL
        let outputFile = outputDir.appending(path: "TestCredentials.swift")
        return [.buildCommand(
            displayName: "Generating test credentials",
            executable: try context.tool(named: "GenerateTestCredentialsTool").url,
            arguments: [outputFile.path],
            inputFiles: [],
            outputFiles: [outputFile]
        )]
    }
}
