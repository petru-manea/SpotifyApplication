class SongGenrService {
  url = "http://localhost:8081/api/spotify";
  upload(file) {
    let formData = new FormData();
    let jsonBodyData = { description: "some", filename: file.name };
    formData.append("file", file);
    formData.append(
      "processedAudioDto",
      new Blob([JSON.stringify(jsonBodyData)], { type: "application/json" })
    );
    return fetch(this.url + "/classify", {
      method: "POST",
      body: formData,
    })
      .then((response) => response.json())
      .catch((error) => console.log("error occurred!", error));
  }

  getAudioById() {
    return fetch(this.url + "/song/533/audio");
  }
}

export default new SongGenrService();
