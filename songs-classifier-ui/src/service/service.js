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

  getSongAudioById(id) {
    return fetch(this.url + `/song/${id}/audio`)
      .then((response) => {
        // read() returns a promise that resolves
        // when a value has been received
        return response.body
          .getReader()
          .read()
          .then((result) => {
            return result;
          });
      })
      .catch((error) => console.log("error occurred!", error));
  }
  getSongs() {
    return fetch(this.url + "/song/all")
      .then((response) => response.json())
      .catch((error) => console.log("error occurred!", error));
  }
  getGenres() {
    return fetch(this.url + "/genres")
      .then((response) => response.json())
      .catch((error) => console.log("error occurred!", error));
  }
}

export default new SongGenrService();
