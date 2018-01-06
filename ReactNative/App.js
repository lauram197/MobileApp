import React from 'react';
import { ListView, AppRegistry} from 'react-native';
import { TabNavigator, StackNavigator } from 'react-navigation';

import Contact from './Modules/Contact';
import ViewElement from './Modules/ViewElement';
import ViewList from './Modules/ViewList';
import AddElement from './Modules/AddElement';
import { MySyncTask, Synchronizer } from './Modules/SyncTask';

global.hotels = [];

const RegallyApp = TabNavigator({
	Home: {
		screen: Contact,
	},
	ViewList: {
		screen: ViewList
	},
},
{
	tabBarPosition: 'top',
	animationEnabled: true,
});


const MainScreenNavigator = StackNavigator({
	Home: { screen: RegallyApp },
	ViewElement: {
		screen: ViewElement,
		path: "ViewElement/:hotel"
	},
    AddElement: {
	    screen: AddElement
    }
});


class MyNav extends React.Component {
    constructor(props)
    {
        super(props);
    }

    componentDidMount() {
        //alert(SyncAdapter.init);
    }

    render() {
        return (
            <MainScreenNavigator screenProps={{
                data_set: new ListView.DataSource({ rowHasChanged: (r1, r2) => true })
            }}/>
        );
    }
}

global.sync = Synchronizer;

AppRegistry.registerComponent('MyNav', ()=> MyNav);
AppRegistry.registerHeadlessTask('TASK_SYNC_ADAPTER', ()=> MySyncTask);

export default MyNav;
