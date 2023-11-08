import requests
from bs4 import BeautifulSoup

def search_and_install_mod(mod_name):
    # Search for the mod on Modrinth
    search_url = f"https://modrinth.com/search?q={mod_name}"
    response = requests.get(search_url)
    soup = BeautifulSoup(response.text, 'html.parser')

    # Find the first search result
    search_results = soup.find_all('div', class_='search-result')
    if len(search_results) > 0:
        first_result = search_results[0]
        mod_slug = first_result['data-slug']

        # Get the mod page
        mod_url = f"https://modrinth.com/mod/{mod_slug}"
        response = requests.get(mod_url)
        soup = BeautifulSoup(response.text, 'html.parser')

        # Find the latest version's download link
        latest_version = soup.find('div', class_='version-card')
        download_link = latest_version.find('a', class_='download-button')['href']

        # Download the jar file
        response = requests.get(download_link)
        with open(f"{mod_name}.jar", 'wb') as file:
            file.write(response.content)

        print(f"Successfully installed {mod_name}!")
    else:
        print(f"No results found for {mod_name}.")

# Example usage
search_and_install_mod('iris')
