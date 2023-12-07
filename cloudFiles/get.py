import modrinth
import tqdm

def getVersion(mod, version):
    results = modrinth.Projects.Search(mod)
    print(f"Mod {results.hits[0].name} with the latest version {results.hits[0].versions[0]} has been found with version {results.hits[0]}")
    project = modrinth.Projects.ModrinthProject(results.hits[0].id)
    version = project.getVersion(project.versions[0])
    for version in tqdm.tqdm(reversed(project.versions)):
        modproject = project.getVersion(version)
        if targetVersion in modproject.gameVersions:
            print(f"found {modproject.gameVersions[0]}")
            return modproject
        else:
            print(f"did not find the correct version instead found {modproject.gameVersions[0]}")
    
    return None

inputFile = "input.txt"
outputFile = "mods.txt"

# load the wanted mods
wantedMods = open(inputFile, "r").read().splitlines()
outUrls = []

targetVersion = "1.20.4"

print(f"program started with {len(wantedMods)} mods. Note that this program is not optimized and will take a while to run.")

for mod in wantedMods:
    project = getVersion(mod, targetVersion)

    try:
        url = project.files[0]["url"] # Returns the hash of the primary file
        print(url) # Returns the download URL of the primary file
        outUrls.append(url)
    except:
        print(f"Could not find the mod {mod} with version {targetVersion}")

with open(outputFile, "w") as f:
    f.write("\n".join(outUrls))