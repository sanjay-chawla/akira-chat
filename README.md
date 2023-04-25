# AkiraBot

AkiraBot is a chatbot Android application that can answer questions about various topics such as general knowledge, entertainment, and technology. It is powered by OpenAI's GPT-3.5 language model, which allows it to generate high-quality responses to user queries.

## Getting Started

### Prerequisites

- Android Studio
- OpenAI API key

### Installation

1. Clone this repository to your local machine.
2. Open Android Studio and import the project.
3. Add your OpenAI API key file to `app/src/main/java/com/example/akirabot/api/API_KEY.kt`. Here is how it should look like:
    ```
    package com.example.akirabot.api
   
    object API_KEY {
        val api_key_value = "your-api-key"
    }
    ```
4. Build and run the project on your Android device or emulator.

## Usage

To use AkiraBot, simply type in your question or query and tap the send button. The chatbot will generate a response based on its training data and the information available on the internet. You can also see a typing indicator when the chatbot is generating a response.

## Contributing

Contributions to this project are welcome! If you find any bugs or issues, please open an issue on this repository. Pull requests are also welcome.

## License

This project is licensed under the MIT License. See the `LICENSE` file for more information.