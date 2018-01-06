import React from 'react';
import { StyleSheet, Text, ScrollView, TextInput, Button, Picker} from 'react-native';

class AddElement extends React.Component {
    static navigationOptions = {
    };
	
    constructor(props) {
        super(props);
        this.state = { location: "", name: "", description: "Description", rating: "" };
    }

    create() {
        let hotel = this.state;
        for(let i = 0; i < global.hotels.length;i++){
            if(global.hotels[i].location.localeCompare(hotel.location)===0){
                alert("Can't have two hotels in the same location!");
                return;
            }
        }

        global.hotels.push({ location:this.state.location, name:this.state.name, description:this.state.description, rating:this.state.rating });
        global.viewlist.update_callback();
        global.sync.addOne(hotel.location, hotel);

        this.props.navigation.goBack();
    }

    render() {
        return (
            <ScrollView style={styles.container}>
                <Text>Location</Text>
                <TextInput style={styles.defaultTextInput} value={this.state.location.toString()} onChangeText={(location)=>this.setState({location})}/>
				<Text>Name</Text>
                <TextInput style={styles.defaultTextInput} value={this.state.name.toString()} onChangeText={(name)=>this.setState({name})}/>
                <Picker selectedValue={this.state.description} onValueChange={(itemValue, itemIndex) => this.setState({description: itemValue})}>
                    <Picker.Item label="Description1" value="Description1" />
                    <Picker.Item label="Description2" value="Description2" />
					<Picker.Item label="Description3" value="Description3" />
                </Picker>
                <Text>Rating</Text>
                <TextInput style={styles.defaultTextInput} value={this.state.rating.toString()} onChangeText={(rating)=>this.setState({rating})}/>
                <Button style={styles.pinkButton} title="Create" onPress={()=>this.create()}/>
            </ScrollView>
        );
    }
}

const styles = StyleSheet.create({
    navbar: {
        flex: 1,
        flexDirection: "row",
        justifyContent: "space-around"
    },
    container: {
        paddingVertical: 60
    },
    titleText: {
        fontSize:48
    },
    defaultTextInput: {
        height:40
    },
    pinkButton: {
        backgroundColor: "#970f99"
    }
});

export default AddElement;
