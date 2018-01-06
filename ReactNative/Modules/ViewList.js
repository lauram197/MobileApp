import React from 'react';
import { StyleSheet, Text, View, Button, ListView, TouchableHighlight} from 'react-native';

class ViewList extends React.Component {
    static navigationOptions = {
        tabBarLabel: 'ViewList',
    };
	
    constructor(props) {
        // console.log(JSON.stringify("CONSTRUCTOR"));

        super(props);
        // force re-render when data-set is changed.
        // const data_set = new ListView.DataSource({rowHasChanged: (r1, r2) => true});
        global.viewlist = this;
        global.sync.getAll();
        this.state = { dataSource: this.props.screenProps.data_set.cloneWithRows(global.hotels) };
    }

    update_callback()
    {
        // console.log(JSON.stringify(global.hotels));
        // console.log(JSON.stringify(this.state.dataSource));
        this.setState({dataSource: this.state.dataSource.cloneWithRows(global.hotels) });
    }

    do_edit(row_data) {
        this.props.navigation.navigate("ViewElement", {data:row_data});
    }

    do_create() {
        this.props.navigation.navigate("AddElement");
    }

    render_row(row_data) {
        return(
            <TouchableHighlight onPress={()=> this.do_edit(row_data)}>
                <Text>
                    { row_data.location + " : " + row_data.name }
                </Text>
            </TouchableHighlight>
        );
    }

    render() {
        return (
            <View>
                <Button style={styles.pinkButton} title={"Create"} onPress={() => this.do_create()}/>
                <ListView
                    dataSource={this.state.dataSource}
                    renderRow={(rowData)=>this.render_row(rowData)}
                />
            </View>
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
    pinkButton: {
        backgroundColor: "#970f99"
    }
});

export default ViewList;
