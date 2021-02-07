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
      .catch((error) => console.error("error occurred!", error));
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
      .catch((error) => console.error("error occurred!", error));
  }
  getSongs(id) {
    const idString = id.toLowerCase();
    if (idString === "all") {
      return fetch(this.url + "/song/all")
        .then((response) => response.json())
        .catch((error) => console.error("error occurred!", error));
    } else {
      return fetch(this.url + "/song?genre="+idString)
        .then((response) => response.json())
        .catch((error) => console.error("error occurred!", error));
    }
  }
}

export default new SongGenrService();
